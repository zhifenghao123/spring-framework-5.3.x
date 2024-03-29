/*
 * Copyright 2002-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.beans.factory.support;

import java.beans.ConstructorProperties;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;

import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.core.CollectionFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.MethodInvoker;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/**
 * Delegate for resolving constructors and factory methods.
 *
 * <p>Performs constructor resolution through argument matching.
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @author Mark Fisher
 * @author Costin Leau
 * @author Sebastien Deleuze
 * @author Sam Brannen
 * @since 2.0
 * @see #autowireConstructor
 * @see #instantiateUsingFactoryMethod
 * @see AbstractAutowireCapableBeanFactory
 */
class ConstructorResolver {

	private static final Object[] EMPTY_ARGS = new Object[0];

	/**
	 * Marker for autowired arguments in a cached argument array, to be replaced
	 * by a {@linkplain #resolveAutowiredArgument resolved autowired argument}.
	 */
	private static final Object autowiredArgumentMarker = new Object();

	private static final NamedThreadLocal<InjectionPoint> currentInjectionPoint =
			new NamedThreadLocal<>("Current injection point");


	private final AbstractAutowireCapableBeanFactory beanFactory;

	private final Log logger;


	/**
	 * Create a new ConstructorResolver for the given factory and instantiation strategy.
	 * @param beanFactory the BeanFactory to work with
	 */
	public ConstructorResolver(AbstractAutowireCapableBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
		this.logger = beanFactory.getLogger();
	}


	/**
	 * "autowire constructor" (with constructor arguments by type) behavior.
	 * Also applied if explicit constructor argument values are specified,
	 * matching all remaining arguments with beans from the bean factory.
	 * <p>This corresponds to constructor injection: In this mode, a Spring
	 * bean factory is able to host components that expect constructor-based
	 * dependency resolution.
	 * @param beanName the name of the bean
	 * @param mbd the merged bean definition for the bean
	 * @param chosenCtors chosen candidate constructors (or {@code null} if none)
	 * @param explicitArgs argument values passed in programmatically via the getBean method,
	 * or {@code null} if none (-> use constructor argument values from bean definition)
	 * @return a BeanWrapper for the new instance
	 */
	/**
	 * 确定构造函数、构造函数参数，然后调用 instantiate 方法进行 bean 的实例化。
	 * 总体思路：
	 * 1、先检查是否指定了具体的构造方法和构造方法参数值，或者在BeanDefinition中缓存了具体的构造方法或构造方法参数值，
	 *   如果存在那么则直接使用该构造方法进行实例化。
	 * 2、如果没有确定的构造方法或构造方法参数值，又分为以下流程：
	 * a. 如果没有确定构造方法，那么则找出类中所有的构造方法。
	 * b. 如果只有一个无参的构造方法，那么直接使用无参的构造方法进行实例化。
	 * c. 如果有多个构造方法或者当前Bean的注入方式是构造方法自动注入，则要自动选择构造方法。
	 * d. 根据所指定的构造方法参数值，确定所需要的最少的构造方法参数值的个数。如果没有指定，从 BeanDefinition 的 constructorArgumentValues属性获取。
	 * e. 对所有的构造方法进行排序，public 构造函数优先参数数量降序，非 public构造函数参数数量降序。
	 * f. 遍历每个构造方法。
	 * g. 如果调用 getBean 方法时，没有显示指定构造方法参数值，那么则根据当前循环到的构造方法得到构造参数类型、构造参数名称与解析后
	 * 的构造方法参数值(resolvedValues)进行匹配，构建ArgumentsHolder 对象。
	 * h. 如果调用 getBean 方法时，指定构造方法参数值，就直接利用传入的构造方法参数值构建 ArgumentsHolder 对象。
	 * i.如果根据当前构造方法找到了对应的构造方法参数值，那么这个构造方法就是可用的，但是不一定这个构造方法就是最佳的，
	 * 所以这里会涉及到是否有多个构造方法匹配了同样的值，这个时候就会用值和构造方法类型进行匹配程度的打分，找到一个最匹配的（分越少优先级越高）。
	 */
	public BeanWrapper autowireConstructor(String beanName, RootBeanDefinition mbd,
			@Nullable Constructor<?>[] chosenCtors, @Nullable Object[] explicitArgs) {

		BeanWrapperImpl bw = new BeanWrapperImpl();
		this.beanFactory.initBeanWrapper(bw);

		//最终需要使用的构造方法变量
		Constructor<?> constructorToUse = null;
		ArgumentsHolder argsHolderToUse = null;
		//最终需要使用的参数变量
		Object[] argsToUse = null;

		// getBean()方法指定了构造方法参数
		if (explicitArgs != null) {
			argsToUse = explicitArgs;
		}
		else {
			// 从缓存中获取构造方法和构造方法参数
			// 当作用域为原型时并多次调用 getBean()时没有传递参数, 创建 Bean 是会走这段缓存逻辑。
			// 为单例只会从一次，之后的 getBean() 都会从单例池获取
			Object[] argsToResolve = null;
			synchronized (mbd.constructorArgumentLock) {
				// resolvedConstructorOrFactoryMethod:缓存已解析的构造函数或工厂方法
				constructorToUse = (Constructor<?>) mbd.resolvedConstructorOrFactoryMethod;
				// 找到了mbd中缓存的构造方法
				// constructorArgumentsResolved：将构造函数参数标记为已解析;true就是标记为了已解析
				if (constructorToUse != null && mbd.constructorArgumentsResolved) {
					// resolvedConstructorArguments：获得已完全解析的构造函数参数(参数类型已经确定，能够直接进行使用)
					// 正常情况下 resolvedConstructorArguments 的值就是 null
					// Found a cached constructor...
					argsToUse = mbd.resolvedConstructorArguments;
					if (argsToUse == null) {
						//获得部分准备好的构造函数参数(该参数的类型是不确定的，需要进行解析)
						argsToResolve = mbd.preparedConstructorArguments;
					}
				}
			}
			//如果存在构造函数参数，那么则对参数值进行类型转化
			//如给定方法的构造函数 Person(int) 则通过此方法后就会把配置中的 "5“ 转换为 5
			//<constructor-arg index="0" value="5"/>
			//缓存中的值可能是原始值也可能是最终值
			if (argsToResolve != null) {
				argsToUse = resolvePreparedArguments(beanName, mbd, bw, constructorToUse, argsToResolve);
			}
		}

		// 如果待使用的构造方法为null，或待使用的构造方法参数为null，也就是没有缓存
		// 这个if代码很长，但其实就去找构造方法、构造方法参并赋值给 constructorToUse、argsToUse
		if (constructorToUse == null || argsToUse == null) {
			// chosenCtors表示所指定的构造方法，没有指定则获取beanClass中的所有的构造方法作为候选者，从这些构造方法中选择一个构造方法
			// Take specified constructors, if any.
			Constructor<?>[] candidates = chosenCtors;
			if (candidates == null) {
				Class<?> beanClass = mbd.getBeanClass();
				try {
					// mbd.isNonPublicAccessAllowed()：默认为true
					// getDeclaredConstructors()：获得本类所有构造方法
					// getConstructors:获得本类的所有公有构造方法
					candidates = (mbd.isNonPublicAccessAllowed() ?
							beanClass.getDeclaredConstructors() : beanClass.getConstructors());
				}
				catch (Throwable ex) {
					throw new BeanCreationException(mbd.getResourceDescription(), beanName,
							"Resolution of declared constructors on bean Class [" + beanClass.getName() +
							"] from ClassLoader [" + beanClass.getClassLoader() + "] failed", ex);
				}
			}

			// 如果只有一个构造方法，并且没有显示指定构造方法参数，并且在xml中没有使用constructor-arg标签，
			// 则需要判断是不是无参构造方法，如果是 则使用无参构造方法进行实例化
			if (candidates.length == 1 && explicitArgs == null && !mbd.hasConstructorArgumentValues()) {
				Constructor<?> uniqueCandidate = candidates[0];
				// 判断是不是无参构造方法
				if (uniqueCandidate.getParameterCount() == 0) {
					synchronized (mbd.constructorArgumentLock) {
						// 确定了构造方法之后进行缓存
						mbd.resolvedConstructorOrFactoryMethod = uniqueCandidate;
						mbd.constructorArgumentsResolved = true;
						mbd.resolvedConstructorArguments = EMPTY_ARGS;
					}
					//进行实例化
					bw.setBeanInstance(instantiate(beanName, mbd, uniqueCandidate, EMPTY_ARGS));
					return bw;
				}
			}

			// 如果入参 chosenCtors不为null,也就是找到了构造方法，或者autowireMode是构造方法自动注入，则可能要自动选择构造方法
			// Need to resolve the constructor.
			boolean autowiring = (chosenCtors != null ||
					mbd.getResolvedAutowireMode() == AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR);
			// 记录解析后的构造方法参数值
			ConstructorArgumentValues resolvedValues = null;

			// minNrOfArgs：表示所有构造方法中，参数个数最少的构造方法的参数个数是多少
			int minNrOfArgs;
			if (explicitArgs != null) {
				minNrOfArgs = explicitArgs.length;
			}
			else {
				// 从BeanDefinition中获取所设置的构造方法参数值，值来源于 constructor-arg标签中的 index属性的值 这个值可以随便写
				ConstructorArgumentValues cargs = mbd.getConstructorArgumentValues();
				// 记录解析后的构造方法参数值
				resolvedValues = new ConstructorArgumentValues();
				// 解析参数个数 值来源于 constructor-arg标签中的 index属性的值 这个值可以随便写
				minNrOfArgs = resolveConstructorArguments(beanName, mbd, bw, cargs, resolvedValues);
			}

			// 按构造方法的参数个数降序排序，先排序public构造函数，参数降序排列
			// 然后排序非public 的构造函数，参数降序排列
			AutowireUtils.sortConstructors(candidates);
			int minTypeDiffWeight = Integer.MAX_VALUE;
			Set<Constructor<?>> ambiguousConstructors = null;
			Deque<UnsatisfiedDependencyException> causes = null;

			// 遍历构造方法，找到一个最合适的
			// 先看参数列表最长的构造方法，根据每个参数的参数类型和参数名去找bean
			for (Constructor<?> candidate : candidates) {
				// 当前构造方法的参数个数
				int parameterCount = candidate.getParameterCount();

				// 已经找到选用的构造函数 且该参数个数大于当前遍历的，则不用继续遍历了
				// 上面已经按照参数个数降序排列了
				if (constructorToUse != null && argsToUse != null && argsToUse.length > parameterCount) {
					// Already found greedy constructor that can be satisfied ->
					// do not look any further, there are only less greedy constructors left.
					break;
				}
				// 在遍历某个构造方法时，如果当前遍历的参数个数小于所指定的参数个数，则忽略该构造方法
				// minNrOfArgs已经是最小的了，比他还小，肯定是不符合我所需要的，就不必往下执行了
				if (parameterCount < minNrOfArgs) {
					continue;
				}

				ArgumentsHolder argsHolder;
				// 当前遍历到的构造方法的参数类型
				Class<?>[] paramTypes = candidate.getParameterTypes();
				// 当 getBean()方法没有显示指定构造方法参数，resolvedValues 不为 null
				if (resolvedValues != null) {
					try {
						// 获取参数名，查看是否在构造方法上使用@ConstructorProperties注解来定义构造方法参数的名字
						String[] paramNames = ConstructorPropertiesChecker.evaluate(candidate, parameterCount);
						if (paramNames == null) {
							// ParameterNameDiscoverer用于解析方法、构造函数上的参数名称
							ParameterNameDiscoverer pnd = this.beanFactory.getParameterNameDiscoverer();
							if (pnd != null) {
								//获取构造函数的参数名称
								paramNames = pnd.getParameterNames(candidate);
							}
						}
						// 根据当前构造方法的参数类型和参数名从beanFactory中得到bean作为参数值
						// resolvedValues：解析后的构造方法参数值
						// paramTypes：当前构造方法中每个参数的属性类型
						// paramNames：当前构造方法中每个参数的属性名称
						// getUserDeclaredConstructor(candidate)：获取父类中被重写的构造方法
						argsHolder = createArgumentArray(beanName, mbd, resolvedValues, bw, paramTypes, paramNames,
								getUserDeclaredConstructor(candidate), autowiring, candidates.length == 1);
					}
					catch (UnsatisfiedDependencyException ex) {
						// 如果找不到相匹配的，也不会直接报错，只能说明当前遍历的构造方法不能用
						if (logger.isTraceEnabled()) {
							logger.trace("Ignoring constructor [" + candidate + "] of bean '" + beanName + "': " + ex);
						}
						// Swallow and try next constructor.
						if (causes == null) {
							causes = new ArrayDeque<>(1);
						}
						causes.add(ex);
						continue;
					}
				}
				else {
					// getBean()方法指定了构造方法参数值
					// 当前构造方法参数个数与传入的参数个数不相等，跳出本次循环
					// Explicit arguments given -> arguments length must match exactly.
					if (parameterCount != explicitArgs.length) {
						continue;
					}
					// 如果参数个数匹配，则把所有参数值封装为一个ArgumentsHolder对象
					argsHolder = new ArgumentsHolder(explicitArgs);
				}

				// 执行到这里，表示当前构造方法可用，并且也找到了对应的构造方法参数值
				// 但是还需要判断，当前构造方法是不是最合适的，也许还有另外的构造方法更合适
				// 根据参数类型和参数值计算权重
				// Lenient宽松，默认宽松模式是开启的
				// 严格模式：解析构造函数时，必须所有的都需要匹配，否则抛出异常
				// 宽松模式：使用具有"最接近的模式"进行匹配
				int typeDiffWeight = (mbd.isLenientConstructorResolution() ?
						argsHolder.getTypeDifferenceWeight(paramTypes) : argsHolder.getAssignabilityWeight(paramTypes));
				// 如果当前构造方法的权重比较小，则表示当前构造方法更合适
				/**
				 * 为什么分越少优先级越高？
				 * 主要是计算找到的bean和构造方法参数类型匹配程度有多高
				 * 假设bean的类型为A，A的父类是B，B的父类是C，同时A实现了接口D
				 * 如果构造方法的参数类型为A，那么完全匹配，得分为0
				 * 如果构造方法的参数类型为B，那么得分为2
				 * 如果构造方法的参数类型为C，那么得分为4
				 * 如果构造方法的参数类型为D，那么得分为1
				 */
				// 将当前构造方法和所找到参数值作为待使用的
				// 遍历下一个构造方法
				// Choose this constructor if it represents the closest match.
				if (typeDiffWeight < minTypeDiffWeight) {
					constructorToUse = candidate;
					argsHolderToUse = argsHolder;
					argsToUse = argsHolder.arguments;
					minTypeDiffWeight = typeDiffWeight;
					ambiguousConstructors = null;
				}
				else if (constructorToUse != null && typeDiffWeight == minTypeDiffWeight) {
					// 如果权重一样，则记录在ambiguousConstructors中，继续遍历下一个构造方法
					if (ambiguousConstructors == null) {
						ambiguousConstructors = new LinkedHashSet<>();
						ambiguousConstructors.add(constructorToUse);
					}
					ambiguousConstructors.add(candidate);
				}
			}

			// 遍历完所有构造方法后，没有找到合适的构造方法，则报错
			if (constructorToUse == null) {
				if (causes != null) {
					UnsatisfiedDependencyException ex = causes.removeLast();
					for (Exception cause : causes) {
						this.beanFactory.onSuppressedException(cause);
					}
					throw ex;
				}
				throw new BeanCreationException(mbd.getResourceDescription(), beanName,
						"Could not resolve matching constructor on bean class [" + mbd.getBeanClassName() + "] " +
						"(hint: specify index/type/name arguments for simple parameters to avoid type ambiguities)");
			}
			// 如果存在权重一样的构造方法并且不是宽松模式，也报错
			// 因为权重一样，Spring不知道该用哪个
			// 如果是宽松模式则不会报错，Spring会用找到的第一个
			else if (ambiguousConstructors != null && !mbd.isLenientConstructorResolution()) {
				throw new BeanCreationException(mbd.getResourceDescription(), beanName,
						"Ambiguous constructor matches found on bean class [" + mbd.getBeanClassName() + "] " +
						"(hint: specify index/type/name arguments for simple parameters to avoid type ambiguities): " +
						ambiguousConstructors);
			}

			// 如果不是通过getBean()方法指定的参数，那么就把找到的构造方法参数进行缓存
			if (explicitArgs == null && argsHolderToUse != null) {
				// 缓存找到的构造方法
				argsHolderToUse.storeCache(mbd, constructorToUse);
			}
		}

		// 得到了构造方法和构造方法的参数值之后，就可以进行实例化了
		Assert.state(argsToUse != null, "Unresolved constructor arguments");
		bw.setBeanInstance(instantiate(beanName, mbd, constructorToUse, argsToUse));
		return bw;
	}

	private Object instantiate(
			String beanName, RootBeanDefinition mbd, Constructor<?> constructorToUse, Object[] argsToUse) {

		try {
			InstantiationStrategy strategy = this.beanFactory.getInstantiationStrategy();
			if (System.getSecurityManager() != null) {
				return AccessController.doPrivileged((PrivilegedAction<Object>) () ->
						strategy.instantiate(mbd, beanName, this.beanFactory, constructorToUse, argsToUse),
						this.beanFactory.getAccessControlContext());
			}
			else {
				return strategy.instantiate(mbd, beanName, this.beanFactory, constructorToUse, argsToUse);
			}
		}
		catch (Throwable ex) {
			throw new BeanCreationException(mbd.getResourceDescription(), beanName,
					"Bean instantiation via constructor failed", ex);
		}
	}

	/**
	 * Resolve the factory method in the specified bean definition, if possible.
	 * {@link RootBeanDefinition#getResolvedFactoryMethod()} can be checked for the result.
	 * @param mbd the bean definition to check
	 */
	public void resolveFactoryMethodIfPossible(RootBeanDefinition mbd) {
		Class<?> factoryClass;
		boolean isStatic;
		if (mbd.getFactoryBeanName() != null) {
			factoryClass = this.beanFactory.getType(mbd.getFactoryBeanName());
			isStatic = false;
		}
		else {
			factoryClass = mbd.getBeanClass();
			isStatic = true;
		}
		Assert.state(factoryClass != null, "Unresolvable factory class");
		factoryClass = ClassUtils.getUserClass(factoryClass);

		Method[] candidates = getCandidateMethods(factoryClass, mbd);
		Method uniqueCandidate = null;
		for (Method candidate : candidates) {
			if (Modifier.isStatic(candidate.getModifiers()) == isStatic && mbd.isFactoryMethod(candidate)) {
				if (uniqueCandidate == null) {
					uniqueCandidate = candidate;
				}
				else if (isParamMismatch(uniqueCandidate, candidate)) {
					uniqueCandidate = null;
					break;
				}
			}
		}
		mbd.factoryMethodToIntrospect = uniqueCandidate;
	}

	private boolean isParamMismatch(Method uniqueCandidate, Method candidate) {
		int uniqueCandidateParameterCount = uniqueCandidate.getParameterCount();
		int candidateParameterCount = candidate.getParameterCount();
		return (uniqueCandidateParameterCount != candidateParameterCount ||
				!Arrays.equals(uniqueCandidate.getParameterTypes(), candidate.getParameterTypes()));
	}

	/**
	 * Retrieve all candidate methods for the given class, considering
	 * the {@link RootBeanDefinition#isNonPublicAccessAllowed()} flag.
	 * Called as the starting point for factory method determination.
	 */
	private Method[] getCandidateMethods(Class<?> factoryClass, RootBeanDefinition mbd) {
		if (System.getSecurityManager() != null) {
			return AccessController.doPrivileged((PrivilegedAction<Method[]>) () ->
					(mbd.isNonPublicAccessAllowed() ?
						ReflectionUtils.getAllDeclaredMethods(factoryClass) : factoryClass.getMethods()));
		}
		else {
			return (mbd.isNonPublicAccessAllowed() ?
					ReflectionUtils.getAllDeclaredMethods(factoryClass) : factoryClass.getMethods());
		}
	}

	/**
	 * Instantiate the bean using a named factory method. The method may be static, if the
	 * bean definition parameter specifies a class, rather than a "factory-bean", or
	 * an instance variable on a factory object itself configured using Dependency Injection.
	 * <p>Implementation requires iterating over the static or instance methods with the
	 * name specified in the RootBeanDefinition (the method may be overloaded) and trying
	 * to match with the parameters. We don't have the types attached to constructor args,
	 * so trial and error is the only way to go here. The explicitArgs array may contain
	 * argument values passed in programmatically via the corresponding getBean method.
	 * @param beanName the name of the bean
	 * @param mbd the merged bean definition for the bean
	 * @param explicitArgs argument values passed in programmatically via the getBean
	 * method, or {@code null} if none (-> use constructor argument values from bean definition)
	 * @return a BeanWrapper for the new instance
	 */
	/**
	 * 主要逻辑：确定工厂对象，然后确认构造函数和构造参数，最后调用 InstantiationStrategy 对象的 instantiate() 来创建实例。
	 * 1、首先确定 factory-bean 属性的值，如果值不为 null，则代表是实例工厂方式实例对象，调用 beanFactory.getBean() 获取工厂对象。
	 *    若为空，则代表是静态工厂方式，对于静态工厂方法必须提供工厂类的全类名，同时设置 factoryBean = null，isStatic = true。
	 *    这里就确定了工厂对象。
	 * 2、工厂对象确定后，则是确认构造参数。
	 *    构造参数的确认主要分为三种情况：explicitArgs 参数、缓存中获取、配置文件中解析。explicitArgs 参数也就是我们在调用 getBean
	 *    方法时指定的方法参数。
	 *    如果explicitArgs不为空，则可以确认方法参数就是它，那也就没必要从缓存中获取了。只需要确定工厂方法就好了。
	 *    如果explicitArgs为空，则需要从缓存中确定了，在缓存中可以确定工厂方法和构造参数。
	 *    如果工厂方法和构造参数都确定好了直接调用 InstantiationStrategy 对象的 instantiate() 来创建实例。
	 * 3、如果explicitArgs 参数为空、在缓存中也没有确定，那就只能从配置文件中获取构造参数信息。
	 *    如果你阅读了笔者前面的文章就会知道，配置文件中的信息都会转换为 BeanDefinition 对象，所以可以通过BeanDefinition 对象进行获取。
	 *    3.1、首先获取到工厂对象里的所有方法，包括工厂对象父类的方法，然后根据条件进行筛选。如果筛选出来的方法数量为 1 并且explicitArgs 参数为空，
	 *    配置文件也没有使用 constructor-arg 属性，则使用无参工厂方法进行实例对象，调用 InstantiationStrategy 对象的 instantiate() 来创建实例。
	 *    顺便进行缓存:
	 *    3.2、如果筛选出来的方法数量大于 1 ，对其进行排序处理，排序规则是：public 构造函数优先，参数数量降序；然后是非 public 构造参数数量降序。
	 *    如果explicitArgs 参数为空，则从配置文件获取构造参数信息，确定构造参数。
	 *    3.3、通过循环的方式，遍历筛选出来的方法。再次进行筛选，当前方法的参数个数需要大于等于最小的构造方法的参数个数(parameterCount >= minNrOfArgs)。
	 *    如果显示提供了参数（explicitArgs != null），则直接比较两者的参数个数是否相等，如果相等则表示找到了，根据explicitArgs 参数构建 ArgumentsHolder 对象。
	 *    如果没有显示提供参数，则需要获取 ParameterNameDiscoverer 对象，主要用于解析方法、构造函数上的参数名称。
	 *    根据 N 多个参数包装成 ArgumentsHolder 对象，该对象用于保存参数，我们称之为参数持有者。
	 *    当将对象包装成 ArgumentsHolder 对象后，我们就可以通过它来进行构造函数匹配，匹配又分为严格模式和宽松模式，默认为宽松模式。
	 *    也就是会使用argsHolder.getTypeDifferenceWeight(paramTypes)方法进行计算。
	 */
	public BeanWrapper instantiateUsingFactoryMethod(
			String beanName, RootBeanDefinition mbd, @Nullable Object[] explicitArgs) {

		// 构造 BeanWrapperImpl 对象
		BeanWrapperImpl bw = new BeanWrapperImpl();
		// 初始化 BeanWrapperImpl
		// 向 BeanWrapper对象中添加 ConversionService 对象和属性编辑器 PropertyEditor 对象
		this.beanFactory.initBeanWrapper(bw);

		Object factoryBean;
		Class<?> factoryClass;
		// 当前factoryMethod是不是静态的
		boolean isStatic;

		// 获取 factory-bean 属性的值,如果有值就说明是 实例工厂方式实例对象
		String factoryBeanName = mbd.getFactoryBeanName();
		if (factoryBeanName != null) {
			if (factoryBeanName.equals(beanName)) {
				throw new BeanDefinitionStoreException(mbd.getResourceDescription(), beanName,
						"factory-bean reference points back to the same bean definition");
			}
			// 根据 factoryBeanName 获取工厂实例，直接走 getBean 方法
			factoryBean = this.beanFactory.getBean(factoryBeanName);
			// 如果当前Bean是单例并且单例池中存在该beanName的对象 则抛出异常
			if (mbd.isSingleton() && this.beanFactory.containsSingleton(beanName)) {
				throw new ImplicitlyAppearedSingletonException();
			}
			this.beanFactory.registerDependentBean(factoryBeanName, beanName);
			factoryClass = factoryBean.getClass();
			isStatic = false;
		}
		else {
			// 静态工厂方式
			// It's a static factory method on the bean class.
			if (!mbd.hasBeanClass()) {
				// 静态工厂创建bean，必须要提供工厂的全类名
				throw new BeanDefinitionStoreException(mbd.getResourceDescription(), beanName,
						"bean definition declares neither a bean class nor a factory-bean reference");
			}
			factoryBean = null;
			factoryClass = mbd.getBeanClass();
			isStatic = true;
		}

		// 工厂方法
		Method factoryMethodToUse = null;
		ArgumentsHolder argsHolderToUse = null;
		// 参数
		Object[] argsToUse = null;

		// 开发者在调用 getBean 方法的时候指定了方法参数则直接使用
		if (explicitArgs != null) {
			argsToUse = explicitArgs;
		}
		else {
			// 没有指定，则尝试从 mbd 中解析参数
			Object[] argsToResolve = null;
			// 首先尝试从缓存中获取
			synchronized (mbd.constructorArgumentLock) {
				// 获得已解析的构造函数或工厂方法
				// resolvedConstructorOrFactoryMethod:缓存已解析的构造函数或工厂方法
				factoryMethodToUse = (Method) mbd.resolvedConstructorOrFactoryMethod;
				// 找到了mbd中缓存的构造方法
				if (factoryMethodToUse != null && mbd.constructorArgumentsResolved) {
					// 获得已完全解析的构造函数参数(参数类型已经确定，能够直接进行使用)
					// 正常情况下 resolvedConstructorArguments 的值就是 null
					// Found a cached factory method...
					argsToUse = mbd.resolvedConstructorArguments;
					if (argsToUse == null) {
						//获得部分准备好的构造函数参数(该参数的类型是不确定的，需要进行解析)
						argsToResolve = mbd.preparedConstructorArguments;
					}
				}
			}
			// 如果存在构造函数参数，那么则对参数值进行类型转化
			// 如给定方法的构造函数 Person(int) 则通过此方法后就会把配置中的"5“ 转换为 5
			// <constructor-arg index="0" value="5"/>
			// 缓存中的值可能是原始值也可能是最终值
			if (argsToResolve != null) {
				// 什么时候进入这里？ 当作用域为原型时并多次调用 getBean()时没有传递参数
				argsToUse = resolvePreparedArguments(beanName, mbd, bw, factoryMethodToUse, argsToResolve);
			}
		}

		// 如果当前BeanDefinition中没有解析出来具体的factoryMethod对象，或者没有解析出对应的方法参数,也就是没有缓存，第一次进行创建
		// 那什么时候能解析出来缓存呢？当作用域为原型时、多次调用getBean方法时不传入参数
		if (factoryMethodToUse == null || argsToUse == null) {
			// 如果当前类是cglib生成的代理类，则获取其父类，否则返回class本身
			// Need to determine the factory method...
			// Try all methods with this name to see if they match the given arguments.
			factoryClass = ClassUtils.getUserClass(factoryClass);

			// 方法集合
			List<Method> candidates = null;
			// 工厂方法是否唯一
			if (mbd.isFactoryMethodUnique) {
				if (factoryMethodToUse == null) {
					factoryMethodToUse = mbd.getResolvedFactoryMethod();
				}
				if (factoryMethodToUse != null) {
					candidates = Collections.singletonList(factoryMethodToUse);
				}
			}
			if (candidates == null) {
				candidates = new ArrayList<>();
				// 获取工厂类里所有待定方法
				Method[] rawCandidates = getCandidateMethods(factoryClass, mbd);
				// 检索所有方法 将符合条件的方法添加到集合中
				for (Method candidate : rawCandidates) {
					// 当前方法是否包含static修饰符，包含则返回true，否则返回false，然后与 isStatic 比较
					// 当前方法名称是否与 配置的factoryMethod方法名称是否相等
					if (Modifier.isStatic(candidate.getModifiers()) == isStatic && mbd.isFactoryMethod(candidate)) {
						candidates.add(candidate);
					}
				}
			}

			// 找到的方法数量为 1 并且没有传入参数 配置文件也没有使用 constructor-arg 属性
			if (candidates.size() == 1 && explicitArgs == null && !mbd.hasConstructorArgumentValues()) {
				Method uniqueCandidate = candidates.get(0);
				// 该工厂方法的参数个数为 0
				if (uniqueCandidate.getParameterCount() == 0) {
					// 缓存唯一的工厂方法
					mbd.factoryMethodToIntrospect = uniqueCandidate;
					synchronized (mbd.constructorArgumentLock) {
						// 缓存已解析的构造函数或工厂方法
						mbd.resolvedConstructorOrFactoryMethod = uniqueCandidate;
						// 将构造函数参数标记为已解析
						mbd.constructorArgumentsResolved = true;
						// 缓存完全解析的构造函数参数
						mbd.resolvedConstructorArguments = EMPTY_ARGS;
					}
					// 创建对象
					bw.setBeanInstance(instantiate(beanName, mbd, factoryBean, uniqueCandidate, EMPTY_ARGS));
					return bw;
				}
			}

			// 匹配的方法数量 大于 1，进行方法排序
			// 按构造方法的参数个数降序排序，先排序public构造函数，参数降序排列
			// 然后排序非public 的构造函数，参数降序排列
			if (candidates.size() > 1) {  // explicitly skip immutable singletonList
				candidates.sort(AutowireUtils.EXECUTABLE_COMPARATOR);
			}

			// 记录解析后的构造方法参数值
			ConstructorArgumentValues resolvedValues = null;
			// autowireMode 是构造方法自动注入，则要自动选择构造方法
			boolean autowiring = (mbd.getResolvedAutowireMode() == AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR);
			int minTypeDiffWeight = Integer.MAX_VALUE;
			Set<Method> ambiguousFactoryMethods = null;

			// minNrOfArgs：表示所有构造方法中，参数个数最少的构造方法的参数个数是多少
			int minNrOfArgs;
			// 开发者在调用 getBean 方法的时候指定了方法参数则直接使用方法参数的个数
			if (explicitArgs != null) {
				minNrOfArgs = explicitArgs.length;
			}
			else {
				// We don't have arguments passed in programmatically, so we need to resolve the
				// arguments specified in the constructor arguments held in the bean definition.
				// 如果有为这个bean定义的构造函数参数值，则返回true
				if (mbd.hasConstructorArgumentValues()) {
					// 构造函数的参数 值来源于 constructor-arg标签
					ConstructorArgumentValues cargs = mbd.getConstructorArgumentValues();
					resolvedValues = new ConstructorArgumentValues();
					// 解析参数个数 值来源于 constructor-arg 标签中的 index属性的值
					// 也会将该 bean 的构造函数参数解析为 resolvedValues 对象，其中会涉及到其他 bean
					minNrOfArgs = resolveConstructorArguments(beanName, mbd, bw, cargs, resolvedValues);
				}
				else {
					// 没有指定参数 也没有 定义constructor-arg标签
					minNrOfArgs = 0;
				}
			}

			Deque<UnsatisfiedDependencyException> causes = null;

			for (Method candidate : candidates) {
				// 方法的参数个数
				int parameterCount = candidate.getParameterCount();

				// 当前方法的参数个数 大于等于 最小的构造方法的参数个数
				if (parameterCount >= minNrOfArgs) {
					ArgumentsHolder argsHolder;

					// 当前方法每个参数的参数类型
					Class<?>[] paramTypes = candidate.getParameterTypes();
					// 调用getBean()时传递了参数
					if (explicitArgs != null) {
						//已经显式的给出参数->参数长度必须精确匹配,不匹配则跳过当前方法
						// Explicit arguments given -> arguments length must match exactly.
						if (paramTypes.length != explicitArgs.length) {
							continue;
						}
						// 参数长度已经匹配 根据 genBean()方法传入的参数构建 ArgumentsHolder 对象
						argsHolder = new ArgumentsHolder(explicitArgs);
					}
					else {
						// 调用getBean()时没有传递参数
						// Resolved constructor arguments: type conversion and/or autowiring necessary.
						try {
							String[] paramNames = null;
							// ParameterNameDiscoverer用于解析方法、构造函数上的参数名称
							ParameterNameDiscoverer pnd = this.beanFactory.getParameterNameDiscoverer();
							if (pnd != null) {
								// 获取指定方法的参数名称
								paramNames = pnd.getParameterNames(candidate);
							}
							// 在获得 解析的构造函数参数值(resolvedValues) 的情况下，创建一个ArgumentsHolder 对象
							argsHolder = createArgumentArray(beanName, mbd, resolvedValues, bw,
									paramTypes, paramNames, candidate, autowiring, candidates.size() == 1);
						}
						catch (UnsatisfiedDependencyException ex) {
							if (logger.isTraceEnabled()) {
								logger.trace("Ignoring factory method [" + candidate + "] of bean '" + beanName + "': " + ex);
							}
							// Swallow and try next overloaded factory method.
							if (causes == null) {
								causes = new ArrayDeque<>(1);
							}
							causes.add(ex);
							continue;
						}
					}

					// 根据参数类型和参数值计算权重。Lenient宽松，默认宽松模式是开启的
					// 严格模式：解析函数时，必须所有的都需要匹配，否则抛出异常
					// 宽松模式：使用具有"最接近的模式"进行匹配
					int typeDiffWeight = (mbd.isLenientConstructorResolution() ?
							argsHolder.getTypeDifferenceWeight(paramTypes) : argsHolder.getAssignabilityWeight(paramTypes));
					// 如果当前方法的权重比较小，则表示当前方法更合适
					// 为什么分越少优先级越高？主要是计算找到的bean和构造方法参数类型匹配程度有多高。
					// 假设bean的类型为 A，A 父类是 B，B 的父类是 C。同时 A 实现了接口 D
					// 如果构造方法的参数类型为A，那么完全匹配，得分为0
					// 如果构造方法的参数类型为B，那么得分为2
					// 如果构造方法的参数类型为C，那么得分为4
					// 如果构造方法的参数类型为D，那么得分为1
					// Choose this factory method if it represents the closest match.
					if (typeDiffWeight < minTypeDiffWeight) {
						factoryMethodToUse = candidate;
						argsHolderToUse = argsHolder;
						argsToUse = argsHolder.arguments;
						minTypeDiffWeight = typeDiffWeight;
						ambiguousFactoryMethods = null;
					}
					// Find out about ambiguity: In case of the same type difference weight
					// for methods with the same number of parameters, collect such candidates
					// and eventually raise an ambiguity exception.
					// However, only perform that check in non-lenient constructor resolution mode,
					// and explicitly ignore overridden methods (with the same parameter signature).
					// 如果具有相同参数数量的方法具有相同的类型差异权重，则收集此类型选项
					// 但是，仅在非宽松构造函数解析模式下执行该检查，并显式忽略重写方法（具有相同的参数签名）
					else if (factoryMethodToUse != null && typeDiffWeight == minTypeDiffWeight &&
							!mbd.isLenientConstructorResolution() &&
							paramTypes.length == factoryMethodToUse.getParameterCount() &&
							!Arrays.equals(paramTypes, factoryMethodToUse.getParameterTypes())) {
						if (ambiguousFactoryMethods == null) {
							ambiguousFactoryMethods = new LinkedHashSet<>();
							ambiguousFactoryMethods.add(factoryMethodToUse);
						}
						ambiguousFactoryMethods.add(candidate);
					}
				}
			}

			// 没有可执行的工厂方法，抛出异常
			if (factoryMethodToUse == null || argsToUse == null) {
				if (causes != null) {
					UnsatisfiedDependencyException ex = causes.removeLast();
					for (Exception cause : causes) {
						this.beanFactory.onSuppressedException(cause);
					}
					throw ex;
				}
				List<String> argTypes = new ArrayList<>(minNrOfArgs);
				if (explicitArgs != null) {
					for (Object arg : explicitArgs) {
						argTypes.add(arg != null ? arg.getClass().getSimpleName() : "null");
					}
				}
				else if (resolvedValues != null) {
					Set<ValueHolder> valueHolders = new LinkedHashSet<>(resolvedValues.getArgumentCount());
					valueHolders.addAll(resolvedValues.getIndexedArgumentValues().values());
					valueHolders.addAll(resolvedValues.getGenericArgumentValues());
					for (ValueHolder value : valueHolders) {
						String argType = (value.getType() != null ? ClassUtils.getShortName(value.getType()) :
								(value.getValue() != null ? value.getValue().getClass().getSimpleName() : "null"));
						argTypes.add(argType);
					}
				}
				String argDesc = StringUtils.collectionToCommaDelimitedString(argTypes);
				throw new BeanCreationException(mbd.getResourceDescription(), beanName,
						"No matching factory method found on class [" + factoryClass.getName() + "]: " +
						(mbd.getFactoryBeanName() != null ?
							"factory bean '" + mbd.getFactoryBeanName() + "'; " : "") +
						"factory method '" + mbd.getFactoryMethodName() + "(" + argDesc + ")'. " +
						"Check that a method with the specified name " +
						(minNrOfArgs > 0 ? "and arguments " : "") +
						"exists and that it is " +
						(isStatic ? "static" : "non-static") + ".");
			}
			else if (void.class == factoryMethodToUse.getReturnType()) {
				throw new BeanCreationException(mbd.getResourceDescription(), beanName,
						"Invalid factory method '" + mbd.getFactoryMethodName() + "' on class [" +
						factoryClass.getName() + "]: needs to have a non-void return type!");
			}
			else if (ambiguousFactoryMethods != null) {
				throw new BeanCreationException(mbd.getResourceDescription(), beanName,
						"Ambiguous factory method matches found on class [" + factoryClass.getName() + "] " +
						"(hint: specify index/type/name arguments for simple parameters to avoid type ambiguities): " +
						ambiguousFactoryMethods);
			}

			if (explicitArgs == null && argsHolderToUse != null) {
				mbd.factoryMethodToIntrospect = factoryMethodToUse;
				// 将解析的构造函数加入缓存
				argsHolderToUse.storeCache(mbd, factoryMethodToUse);
			}
		}

		// 反射调用factoryBean对象中的工厂方法进行实例化得到一个对象
		bw.setBeanInstance(instantiate(beanName, mbd, factoryBean, factoryMethodToUse, argsToUse));
		return bw;
	}

	private Object instantiate(String beanName, RootBeanDefinition mbd,
			@Nullable Object factoryBean, Method factoryMethod, Object[] args) {

		try {
			if (System.getSecurityManager() != null) {
				return AccessController.doPrivileged((PrivilegedAction<Object>) () ->
						this.beanFactory.getInstantiationStrategy().instantiate(
								mbd, beanName, this.beanFactory, factoryBean, factoryMethod, args),
						this.beanFactory.getAccessControlContext());
			}
			else {
				return this.beanFactory.getInstantiationStrategy().instantiate(
						mbd, beanName, this.beanFactory, factoryBean, factoryMethod, args);
			}
		}
		catch (Throwable ex) {
			throw new BeanCreationException(mbd.getResourceDescription(), beanName,
					"Bean instantiation via factory method failed", ex);
		}
	}

	/**
	 * Resolve the constructor arguments for this bean into the resolvedValues object.
	 * This may involve looking up other beans.
	 * <p>This method is also used for handling invocations of static factory methods.
	 *
	 * 首先获得当前 Bean 的 constructor-arg 标签的个数 minNrOfArgs ，然后遍历 cargs 中的 indexedArgumentValues 元素，
	 * 获取每个对象的key(也就是constructor-arg标签的index元素)，与 minNrOfArgs 进行比较。顺便将 key 和 value 添加到 resolvedValues 对象中。
	 */
	private int resolveConstructorArguments(String beanName, RootBeanDefinition mbd, BeanWrapper bw,
			ConstructorArgumentValues cargs, ConstructorArgumentValues resolvedValues) {

		// 获得当前 beanFatory 类型转换器
		TypeConverter customConverter = this.beanFactory.getCustomTypeConverter();
		// 获得当前 beanFatory 类型转换器为 null，则使用 bw，bw 实现了 TypeConverter
		TypeConverter converter = (customConverter != null ? customConverter : bw);
		//为给定的BeanFactory和BeanDefinition创建一个BeanDefinitionValueResolver
		BeanDefinitionValueResolver valueResolver =
				new BeanDefinitionValueResolver(this.beanFactory, beanName, mbd, converter);

		// 获得 constructor-arg 标签的个数
		int minNrOfArgs = cargs.getArgumentCount();

		// 先遍历 args 中的indexedArgumentValues，indexedArgumentValues存的是某个index对应的构造方法参数值
		for (Map.Entry<Integer, ConstructorArgumentValues.ValueHolder> entry : cargs.getIndexedArgumentValues().entrySet()) {
			int index = entry.getKey();
			if (index < 0) {
				throw new BeanCreationException(mbd.getResourceDescription(), beanName,
						"Invalid constructor argument index: " + index);
			}
			if (index + 1 > minNrOfArgs) {
				minNrOfArgs = index + 1;
			}
			// 获得构造方法参数值
			ConstructorArgumentValues.ValueHolder valueHolder = entry.getValue();
			if (valueHolder.isConverted()) {
				// 把该数据添加到 resolvedValues 对象中
				resolvedValues.addIndexedArgumentValue(index, valueHolder);
			}
			else {
				// 把“值”转化为对应的类型
				// 获得 constructor-arg 的 value
				// resolveValueIfNecessary：这里可能会创建bean 因为 constructor-arg 里面有个 ref 属性，可以引用其他bean。
				Object resolvedValue =
						valueResolver.resolveValueIfNecessary("constructor argument", valueHolder.getValue());
				ConstructorArgumentValues.ValueHolder resolvedValueHolder =
						new ConstructorArgumentValues.ValueHolder(resolvedValue, valueHolder.getType(), valueHolder.getName());
				resolvedValueHolder.setSource(valueHolder);
				// 把该数据添加到 resolvedValues 对象中
				resolvedValues.addIndexedArgumentValue(index, resolvedValueHolder);
			}
		}

		// 把genericArgumentValues中的值进行类型转化然后添加到resolvedValues中去
		// 和上述循环逻辑差不多
		for (ConstructorArgumentValues.ValueHolder valueHolder : cargs.getGenericArgumentValues()) {
			if (valueHolder.isConverted()) {
				resolvedValues.addGenericArgumentValue(valueHolder);
			}
			else {
				Object resolvedValue =
						valueResolver.resolveValueIfNecessary("constructor argument", valueHolder.getValue());
				ConstructorArgumentValues.ValueHolder resolvedValueHolder = new ConstructorArgumentValues.ValueHolder(
						resolvedValue, valueHolder.getType(), valueHolder.getName());
				resolvedValueHolder.setSource(valueHolder);
				resolvedValues.addGenericArgumentValue(resolvedValueHolder);
			}
		}

		return minNrOfArgs;
	}

	/**
	 * Create an array of arguments to invoke a constructor or factory method,
	 * given the resolved constructor argument values.
	 * 根据当前构造方法的参数类型、参数名称与上面解析得到的构造方法参数值进行匹配，如果匹配成功返回构造方法的参数值，进行类型转换。
	 * 然后将必要的属性进行赋值，最后进行返回。
	 */
	private ArgumentsHolder createArgumentArray(
			String beanName, RootBeanDefinition mbd, @Nullable ConstructorArgumentValues resolvedValues,
			BeanWrapper bw, Class<?>[] paramTypes, @Nullable String[] paramNames, Executable executable,
			boolean autowiring, boolean fallback) throws UnsatisfiedDependencyException {

		TypeConverter customConverter = this.beanFactory.getCustomTypeConverter();
		TypeConverter converter = (customConverter != null ? customConverter : bw);

		// 根据参数类型的个数初始化出来对应的ArgumentsHolder
		ArgumentsHolder args = new ArgumentsHolder(paramTypes.length);
		Set<ConstructorArgumentValues.ValueHolder> usedValueHolders = new HashSet<>(paramTypes.length);
		Set<String> autowiredBeanNames = new LinkedHashSet<>(4);

		// 循环执行
		for (int paramIndex = 0; paramIndex < paramTypes.length; paramIndex++) {
			// 获得参数类型
			Class<?> paramType = paramTypes[paramIndex];
			// 获得参数名称
			String paramName = (paramNames != null ? paramNames[paramIndex] : "");
			// 尝试查找匹配的构造函数参数值，无论是索引的还是通用的
			// Try to find matching constructor argument value, either indexed or generic.
			ConstructorArgumentValues.ValueHolder valueHolder = null;
			// 如果指定了构造方法参数值，那么则看当前paramType有没有对应的值
			if (resolvedValues != null) {
				// 拿到第paramIndex位置的构造方法参数值，会根据传进去的类型、名称进行匹配，如果类型、名称不相等则返回null
				valueHolder = resolvedValues.getArgumentValue(paramIndex, paramType, paramName, usedValueHolders);
				// If we couldn't find a direct match and are not supposed to autowire,
				// let's try the next generic, untyped argument value as fallback:
				// it could match after type conversion (for example, String -> int).
				// 如果找不到直接匹配项，尝试一个通用的，无类型的参数值作为后备，
				// 类型转换后可以匹配（例如，String-> int）。
				if (valueHolder == null && (!autowiring || paramTypes.length == resolvedValues.getArgumentCount())) {
					valueHolder = resolvedValues.getGenericArgumentValue(null, null, usedValueHolders);
				}
			}
			// 如果找到了对应的值，则进行类型转化，把转化前的值存在args.rawArguments中
			// 转化后的值存在args.arguments中
			if (valueHolder != null) {
				// We found a potential match - let's give it a try.
				// Do not consider the same value definition multiple times!
				usedValueHolders.add(valueHolder);
				Object originalValue = valueHolder.getValue();
				Object convertedValue;
				if (valueHolder.isConverted()) {
					convertedValue = valueHolder.getConvertedValue();
					args.preparedArguments[paramIndex] = convertedValue;
				}
				else {
					// 为给定的方法或构造函数创建一个新的MethodParameter。
					MethodParameter methodParam = MethodParameter.forExecutable(executable, paramIndex);
					try {
						//进行类型转换
						convertedValue = converter.convertIfNecessary(originalValue, paramType, methodParam);
					}
					catch (TypeMismatchException ex) {
						throw new UnsatisfiedDependencyException(
								mbd.getResourceDescription(), beanName, new InjectionPoint(methodParam),
								"Could not convert argument value of type [" +
										ObjectUtils.nullSafeClassName(valueHolder.getValue()) +
										"] to required type [" + paramType.getName() + "]: " + ex.getMessage());
					}
					Object sourceHolder = valueHolder.getSource();
					if (sourceHolder instanceof ConstructorArgumentValues.ValueHolder) {
						Object sourceValue = ((ConstructorArgumentValues.ValueHolder) sourceHolder).getValue();
						// 将 resolveNecessary 变量赋值为 true，该变量会影响
						// resolvedConstructorArguments:缓存完全解析的构造函数参数
						// preparedConstructorArguments:缓存部分准备好的构造函数参数
						// 这两个参数是谁为 null 值
						args.resolveNecessary = true;
						args.preparedArguments[paramIndex] = sourceValue;
					}
				}
				args.arguments[paramIndex] = convertedValue;
				args.rawArguments[paramIndex] = originalValue;
			}
			else {
				// 如果上述逻辑，没有找到对应的值。举例中的举例一 就会走该代码分支
				// 为给定的方法或构造函数创建一个新的MethodParameter。
				MethodParameter methodParam = MethodParameter.forExecutable(executable, paramIndex);
				// No explicit match found: we're either supposed to autowire or
				// have to fail creating an argument array for the given constructor.
				if (!autowiring) {
					throw new UnsatisfiedDependencyException(
							mbd.getResourceDescription(), beanName, new InjectionPoint(methodParam),
							"Ambiguous argument values for parameter of type [" + paramType.getName() +
							"] - did you specify the correct bean references as arguments?");
				}
				// 如果 autowiring 的值为 true，也就是如果入参 chosenCtors不为null,也就是找到了构造方法
				// 或者autowireMode是构造方法自动注入，则可能要自动选择构造方法
				// boolean autowiring = (chosenCtors != null ||
				//         mbd.getResolvedAutowireMode() == AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR);
				try {
					// 该方法内部会调用 resolveDependency 方法,得到一个bean,resolveDependency 方法很重要。
					Object autowiredArgument = resolveAutowiredArgument(
							methodParam, beanName, autowiredBeanNames, converter, fallback);
					args.rawArguments[paramIndex] = autowiredArgument;
					args.arguments[paramIndex] = autowiredArgument;
					args.preparedArguments[paramIndex] = autowiredArgumentMarker;
					// 将 resolveNecessary 变量赋值为 true，该变量会影响
					// resolvedConstructorArguments:缓存完全解析的构造函数参数
					// preparedConstructorArguments:缓存部分准备好的构造函数参数
					// 这两个参数是否为 null
					args.resolveNecessary = true;
				}
				catch (BeansException ex) {
					throw new UnsatisfiedDependencyException(
							mbd.getResourceDescription(), beanName, new InjectionPoint(methodParam), ex);
				}
			}
		}

		for (String autowiredBeanName : autowiredBeanNames) {
			this.beanFactory.registerDependentBean(autowiredBeanName, beanName);
			if (logger.isDebugEnabled()) {
				logger.debug("Autowiring by type from bean name '" + beanName +
						"' via " + (executable instanceof Constructor ? "constructor" : "factory method") +
						" to bean named '" + autowiredBeanName + "'");
			}
		}

		// 最后，返回找到的值
		return args;
	}

	/**
	 * Resolve the prepared arguments stored in the given bean definition.
	 */
	private Object[] resolvePreparedArguments(String beanName, RootBeanDefinition mbd, BeanWrapper bw,
			Executable executable, Object[] argsToResolve) {

		TypeConverter customConverter = this.beanFactory.getCustomTypeConverter();
		TypeConverter converter = (customConverter != null ? customConverter : bw);
		BeanDefinitionValueResolver valueResolver =
				new BeanDefinitionValueResolver(this.beanFactory, beanName, mbd, converter);
		Class<?>[] paramTypes = executable.getParameterTypes();

		Object[] resolvedArgs = new Object[argsToResolve.length];
		for (int argIndex = 0; argIndex < argsToResolve.length; argIndex++) {
			Object argValue = argsToResolve[argIndex];
			MethodParameter methodParam = MethodParameter.forExecutable(executable, argIndex);
			if (argValue == autowiredArgumentMarker) {
				argValue = resolveAutowiredArgument(methodParam, beanName, null, converter, true);
			}
			else if (argValue instanceof BeanMetadataElement) {
				argValue = valueResolver.resolveValueIfNecessary("constructor argument", argValue);
			}
			else if (argValue instanceof String) {
				argValue = this.beanFactory.evaluateBeanDefinitionString((String) argValue, mbd);
			}
			Class<?> paramType = paramTypes[argIndex];
			try {
				resolvedArgs[argIndex] = converter.convertIfNecessary(argValue, paramType, methodParam);
			}
			catch (TypeMismatchException ex) {
				throw new UnsatisfiedDependencyException(
						mbd.getResourceDescription(), beanName, new InjectionPoint(methodParam),
						"Could not convert argument value of type [" + ObjectUtils.nullSafeClassName(argValue) +
						"] to required type [" + paramType.getName() + "]: " + ex.getMessage());
			}
		}
		return resolvedArgs;
	}

	protected Constructor<?> getUserDeclaredConstructor(Constructor<?> constructor) {
		Class<?> declaringClass = constructor.getDeclaringClass();
		Class<?> userClass = ClassUtils.getUserClass(declaringClass);
		if (userClass != declaringClass) {
			try {
				return userClass.getDeclaredConstructor(constructor.getParameterTypes());
			}
			catch (NoSuchMethodException ex) {
				// No equivalent constructor on user class (superclass)...
				// Let's proceed with the given constructor as we usually would.
			}
		}
		return constructor;
	}

	/**
	 * Template method for resolving the specified argument which is supposed to be autowired.
	 */
	@Nullable
	protected Object resolveAutowiredArgument(MethodParameter param, String beanName,
			@Nullable Set<String> autowiredBeanNames, TypeConverter typeConverter, boolean fallback) {

		Class<?> paramType = param.getParameterType();
		if (InjectionPoint.class.isAssignableFrom(paramType)) {
			InjectionPoint injectionPoint = currentInjectionPoint.get();
			if (injectionPoint == null) {
				throw new IllegalStateException("No current InjectionPoint available for " + param);
			}
			return injectionPoint;
		}
		try {
			return this.beanFactory.resolveDependency(
					new DependencyDescriptor(param, true), beanName, autowiredBeanNames, typeConverter);
		}
		catch (NoUniqueBeanDefinitionException ex) {
			throw ex;
		}
		catch (NoSuchBeanDefinitionException ex) {
			if (fallback) {
				// Single constructor or factory method -> let's return an empty array/collection
				// for e.g. a vararg or a non-null List/Set/Map parameter.
				if (paramType.isArray()) {
					return Array.newInstance(paramType.getComponentType(), 0);
				}
				else if (CollectionFactory.isApproximableCollectionType(paramType)) {
					return CollectionFactory.createCollection(paramType, 0);
				}
				else if (CollectionFactory.isApproximableMapType(paramType)) {
					return CollectionFactory.createMap(paramType, 0);
				}
			}
			throw ex;
		}
	}

	static InjectionPoint setCurrentInjectionPoint(@Nullable InjectionPoint injectionPoint) {
		InjectionPoint old = currentInjectionPoint.get();
		if (injectionPoint != null) {
			currentInjectionPoint.set(injectionPoint);
		}
		else {
			currentInjectionPoint.remove();
		}
		return old;
	}


	/**
	 * Private inner class for holding argument combinations.
	 */
	private static class ArgumentsHolder {

		public final Object[] rawArguments;

		public final Object[] arguments;

		public final Object[] preparedArguments;

		public boolean resolveNecessary = false;

		public ArgumentsHolder(int size) {
			this.rawArguments = new Object[size];
			this.arguments = new Object[size];
			this.preparedArguments = new Object[size];
		}

		public ArgumentsHolder(Object[] args) {
			this.rawArguments = args;
			this.arguments = args;
			this.preparedArguments = args;
		}

		public int getTypeDifferenceWeight(Class<?>[] paramTypes) {
			// If valid arguments found, determine type difference weight.
			// Try type difference weight on both the converted arguments and
			// the raw arguments. If the raw weight is better, use it.
			// Decrease raw weight by 1024 to prefer it over equal converted weight.
			int typeDiffWeight = MethodInvoker.getTypeDifferenceWeight(paramTypes, this.arguments);
			int rawTypeDiffWeight = MethodInvoker.getTypeDifferenceWeight(paramTypes, this.rawArguments) - 1024;
			return Math.min(rawTypeDiffWeight, typeDiffWeight);
		}

		public int getAssignabilityWeight(Class<?>[] paramTypes) {
			for (int i = 0; i < paramTypes.length; i++) {
				if (!ClassUtils.isAssignableValue(paramTypes[i], this.arguments[i])) {
					return Integer.MAX_VALUE;
				}
			}
			for (int i = 0; i < paramTypes.length; i++) {
				if (!ClassUtils.isAssignableValue(paramTypes[i], this.rawArguments[i])) {
					return Integer.MAX_VALUE - 512;
				}
			}
			return Integer.MAX_VALUE - 1024;
		}

		public void storeCache(RootBeanDefinition mbd, Executable constructorOrFactoryMethod) {
			synchronized (mbd.constructorArgumentLock) {
				mbd.resolvedConstructorOrFactoryMethod = constructorOrFactoryMethod;
				mbd.constructorArgumentsResolved = true;
				if (this.resolveNecessary) {
					mbd.preparedConstructorArguments = this.preparedArguments;
				}
				else {
					mbd.resolvedConstructorArguments = this.arguments;
				}
			}
		}
	}


	/**
	 * Delegate for checking Java's {@link ConstructorProperties} annotation.
	 */
	private static class ConstructorPropertiesChecker {

		@Nullable
		public static String[] evaluate(Constructor<?> candidate, int paramCount) {
			ConstructorProperties cp = candidate.getAnnotation(ConstructorProperties.class);
			if (cp != null) {
				String[] names = cp.value();
				if (names.length != paramCount) {
					throw new IllegalStateException("Constructor annotated with @ConstructorProperties but not " +
							"corresponding to actual number of parameters (" + paramCount + "): " + candidate);
				}
				return names;
			}
			else {
				return null;
			}
		}
	}

}

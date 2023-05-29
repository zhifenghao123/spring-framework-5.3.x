package com.hao.springmvc.model;

/**
 * Account class
 *
 * @author haozhifeng
 * @date 2023/05/24
 */
public class Account {
    /**
     * 账号id
     */
    private String accountId;
    /**
     * 账号
     */
    private String accountNo;
    /**
     * 账号名
     */
    private String accountName;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}

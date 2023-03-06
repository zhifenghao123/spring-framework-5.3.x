package com.hao.javaConfig6.component;

/**
 * Comment class
 *
 * @author haozhifeng
 * @date 2023/02/27
 */
public class Comment {
    private String commentId;
    private String commentText;
    private Product product;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

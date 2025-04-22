package qiyebao.application.orgmng.empservice;

import org.springframework.stereotype.Component;
import qiyebao.common.framework.domain.CollectionModifier;
import qiyebao.domain.orgmng.emp.Emp;
import qiyebao.domain.orgmng.emp.EmpHandler;
import qiyebao.domain.orgmng.emp.Post;

import java.util.Collection;
import java.util.Objects;

@Component
public class PostsModifier extends CollectionModifier<Emp, Post, String> {
    private final EmpHandler handler;

    PostsModifier(EmpHandler handler) {
        this.handler = handler;
    }

    @Override
    protected Collection<Post> getCurrItems(Emp emp) {
        return emp.getPosts();
    }

    @Override
    protected boolean isSame(Post currPost, String reqPost) {
        return Objects.equals(currPost.getPostTypeCode(), reqPost);
    }

    @Override
    protected void modifyItem(Emp emp, String reqPost, Long userId) {
        // 对于Post实际上不存在修改，所以什么都不用做
    }

    @Override
    protected void removeItem(Emp emp, Post currPost, Long userId) {
        handler.removePost(emp, currPost.getPostTypeCode(), userId);
    }

    @Override
    protected void addItem(Emp emp, String reqPost, Long userId) {
        handler.addPost(emp, reqPost, userId
        );
    }
}
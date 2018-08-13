package com.xujiaji.wanandroid.repository.remote;

import com.xujiaji.wanandroid.repository.bean.BannerBean;
import com.xujiaji.wanandroid.repository.bean.BlogPostBean;
import com.xujiaji.wanandroid.repository.bean.PageBean;
import com.xujiaji.wanandroid.repository.bean.Result;
import com.xujiaji.wanandroid.repository.bean.UserBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * author: xujiaji
 * created on: 2018/8/5 23:04
 * description:
 */
public interface API {

    /**
     * 首页博文
     */
    @GET("article/list/{num}/json")
    Call<Result<PageBean<BlogPostBean>>> getBlogPosts(@Path("num") int num);

    /**
     * 首页项目
     */
    @GET("article/listproject/{num}/json")
    Call<Result<PageBean<BlogPostBean>>> getProjects(@Path("num") int num);

    /**
     * 首页博文顶部banner图
     */
    @GET("banner/json")
    Call<Result<List<BannerBean>>> getBanners();

    /**
     * 登录
     */
    @POST("user/login")
    @FormUrlEncoded
    Call<Result<UserBean>> postLogin(@Field("username") String username, @Field("password") String password);

    /**
     * 注册
     */
    @POST("user/register")
    @FormUrlEncoded
    Call<Result<UserBean>> postRegister(@Field("username") String username, @Field("password") String password, @Field("repassword") String repassword);

    /**
     * OPENAPIS
     */
    @GET("openapis")
    Call<String> getOpenAPIS();
}

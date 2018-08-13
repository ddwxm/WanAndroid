package com.xujiaji.wanandroid.module.main;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xujiaji.mvvmquick.base.MQViewModel;
import com.xujiaji.mvvmquick.util.ActivityUtils;
import com.xujiaji.wanandroid.R;
import com.xujiaji.wanandroid.adapter.FragmentsPagerAdapter;
import com.xujiaji.wanandroid.base.App;
import com.xujiaji.wanandroid.base.BaseActivity;
import com.xujiaji.wanandroid.base.BaseFragment;
import com.xujiaji.wanandroid.databinding.ActivityMainBinding;
import com.xujiaji.wanandroid.helper.PrefHelper;
import com.xujiaji.wanandroid.helper.ToastHelper;
import com.xujiaji.wanandroid.helper.ToolbarHelper;
import com.xujiaji.wanandroid.model.FragmentModel;
import com.xujiaji.wanandroid.module.login.LoginActivity;
import com.xujiaji.wanandroid.repository.bean.UserBean;

import javax.inject.Inject;
import javax.inject.Named;

public class MainActivity extends BaseActivity<ActivityMainBinding, MQViewModel> {

    @Inject
    FragmentsPagerAdapter mDrawerPagerAdapter;

    @Inject
    @Named("Post")
    FragmentModel mBlogModel;

    @Inject
    @Named("Project")
    FragmentModel mProjectModel;

    @Inject
    @Named("Tool")
    FragmentModel mToolModel;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_blog_post:
                showFrag(mBlogModel.getFragment());
                return true;
            case R.id.navigation_project:
                showFrag(mProjectModel.getFragment());
                return true;
            case R.id.navigation_tool:
                showFrag(mToolModel.getFragment());
                return true;
        }
        return false;
    };

    private void showFrag(BaseFragment frag) {
        getSupportFragmentManager()
                .beginTransaction()
                .hide(mBlogModel.getFragment())
                .hide(mProjectModel.getFragment())
                .hide(mToolModel.getFragment())
                .show(frag)
                .commit();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ToolbarHelper.initTranslucent(this);
        super.onCreate(savedInstanceState);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, mBlogModel.getFragment(), "MainBlogPostsFragment")
                .add(R.id.container, mProjectModel.getFragment(), "MainProjectsFragment")
                .add(R.id.container, mToolModel.getFragment(), "MainToolsFragment")
                .hide(mProjectModel.getFragment())
                .hide(mToolModel.getFragment())
                .commit();

        changeAccount(new Gson().fromJson(PrefHelper.getString(PrefHelper.USER_INFO), UserBean.class));
        App.Login.event().observe(this, this::changeAccount);
    }

    @Override
    public void onBinding(ActivityMainBinding binding) {
        super.onBinding(binding);
        ToolbarHelper.initFullBar(binding.includeBar.toolbar, this);
        initDrawer(binding.drawer, binding.includeBar.toolbar);
        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        binding.navMenu.drawerViewPager.setAdapter(mDrawerPagerAdapter);
        binding.navMenu.drawerTabLayout.setupWithViewPager(binding.navMenu.drawerViewPager);
        binding.fab.setOnClickListener(v -> {
            if (App.Login.isOK()) {

            } else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }

    private void changeAccount(UserBean userBean) {
        if (userBean != null) { // login
            TextView tvName = binding.navMenu.extrasNav.getHeaderView(0).findViewById(R.id.navFullName);
            TextView tvEmail = binding.navMenu.extrasNav.getHeaderView(0).findViewById(R.id.navUsername);
            tvName.setText(userBean.getEmail());
            tvEmail.setText(userBean.getUsername());
        } else { // no login

        }

    }

    private void initDrawer(DrawerLayout drawer, Toolbar toolbar) {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
//                updateCounter(binding.navView);
                super.onDrawerOpened(drawerView);
            }
        };

        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawer.isDrawerOpen(Gravity.START)) {
            binding.drawer.closeDrawer(Gravity.START);
            return;
        }
        if (ActivityUtils.exitBy2Click()) {
            super.onBackPressed();
        } else {
            ToastHelper.info(getString(R.string.again_touch_exit));
        }

    }
}

package com.cnl.mybot.ys.signin;

import com.cnl.mybot.ys.signin.entity.Result;

import java.util.List;

public interface SignInCallback {
    void onSingIn(List<Result> results);
}

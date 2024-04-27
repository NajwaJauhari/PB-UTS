package com.example.utspbnajwajauhari.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchUserResponse {
    @SerializedName("items")
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
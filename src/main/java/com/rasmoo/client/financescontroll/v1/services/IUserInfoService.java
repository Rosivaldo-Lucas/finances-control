package com.rasmoo.client.financescontroll.v1.services;

import com.rasmoo.client.financescontroll.entity.User;

public interface IUserInfoService {
    
    public User findAuth() throws Exception;

}

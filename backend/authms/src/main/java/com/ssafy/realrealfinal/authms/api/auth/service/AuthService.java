package com.ssafy.realrealfinal.authms.api.auth.service;

import java.io.IOException;

public interface AuthService {

    String login(String code, String github);
}

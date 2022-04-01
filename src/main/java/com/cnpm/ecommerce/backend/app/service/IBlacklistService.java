package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.dto.BrandDTO;
import com.cnpm.ecommerce.backend.app.dto.LogoutRequest;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.entity.Blacklist;
import com.cnpm.ecommerce.backend.app.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IBlacklistService {

    Optional<Blacklist> findByToken(String token);

    MessageResponse addTokenToBlacklist(LogoutRequest logoutRequest);
}

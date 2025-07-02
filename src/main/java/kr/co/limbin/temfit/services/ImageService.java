package kr.co.limbin.temfit.services;

import kr.co.limbin.temfit.entities.ImageEntity;
import kr.co.limbin.temfit.entities.UserEntity;
import kr.co.limbin.temfit.mappers.ImageMapper;
import kr.co.limbin.temfit.results.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageMapper imageMapper;

    public CommonResult add(UserEntity user, ImageEntity image) {
        if (user == null || user.isDeleted() || user.isSuspended()) {
            return CommonResult.FAILURE_SESSION_EXPIRED;
        }
        if (image == null
                || image.getName() == null
                || image.getContentType() == null
                || image.getData() == null
                || image.getData().length == 0) {
            return CommonResult.FAILURE;
        }
        image.setCreatedAt(LocalDateTime.now());
        return this.imageMapper.insert(image) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }

    public ImageEntity getById(int id) {
        if (id < 1) {
            return null;
        }
        return this.imageMapper.selectById(id);
    }
}

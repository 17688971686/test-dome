package com.sn.framework.core.shiro;

import com.octo.captcha.component.image.backgroundgenerator.GradientBackgroundGenerator;
import com.octo.captcha.component.image.color.SingleColorGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.NonLinearTextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.engine.GenericCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;
import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;

import java.awt.*;

/**
 * JCaptcha验证码
 *
 * @author wyl
 * @date 2016-12-7
 */
public class JCaptchaUtil {

    private static class SingletonHolder {
        private static ImageCaptchaService imageCaptchaService = new DefaultManageableImageCaptchaService(
                new FastHashMapCaptchaStore(),
                new GenericCaptchaEngine(
                        new GimpyFactory[]{new GimpyFactory(
                                new RandomWordGenerator("23456789ABCDEFGHJKLNMPQRSTUVWXYZ"),
                                new ComposedWordToImage(
                                        new RandomFontGenerator(20, 20, new Font[]{new Font("Arial", 20, 20)}),
                                        new GradientBackgroundGenerator(100, 50, new SingleColorGenerator(new Color(235, 255, 255)), new SingleColorGenerator(new Color(255, 195, 230))),
                                        new NonLinearTextPaster(4, 4, new Color(11, 11, 11))
                                )
                        )}
                ),
                180,
                180000,
                20000
        );
    }

    private JCaptchaUtil() {
    }

    public static ImageCaptchaService getInstance() {
        return SingletonHolder.imageCaptchaService;
    }

}

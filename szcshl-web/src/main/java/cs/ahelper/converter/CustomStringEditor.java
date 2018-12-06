package cs.ahelper.converter;

import java.beans.PropertyEditorSupport;

import cs.common.utils.Validate;
import cs.xss.XssShieldUtil;
import org.springframework.web.util.HtmlUtils;

/**
 * String类型转化器
 */
public class CustomStringEditor extends PropertyEditorSupport {

  @Override
  public void setAsText(String text) throws IllegalArgumentException {
    // xss过滤，表单提交时封装参数，String类型会经过此处
    text = XssShieldUtil.getInstance().stripXss(text);
    setValue(text);
  }

  @Override
  public String getAsText() {
    return getValue().toString();
  }
}

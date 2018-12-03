package com.sn.framework.xss;

/**
 * Created by ldm on 2018/11/26 0026.
 */

import com.sn.framework.core.common.StrUtils;

import java.beans.PropertyEditorSupport;

public class StringEscapeEditor extends PropertyEditorSupport {

    private boolean escapeHTML;
    private boolean escapeJavaScript;
    private boolean escapeSQL;

    public StringEscapeEditor() {
        super();
    }

    public StringEscapeEditor(boolean escapeHTML, boolean escapeJavaScript, boolean escapeSQL) {
        super();
        this.escapeHTML = escapeHTML;
        this.escapeJavaScript = escapeJavaScript;
        this.escapeSQL = escapeSQL;
    }

    @Override
    public void setAsText(String text) {
        if (text == null) {
            setValue(null);
        } else {
            String value = text;
            if (escapeHTML) {
                //value = StringEscapeUtils.escapeHtml3(value);
                value = StrUtils.cleanXSS(value);
            }
            if (escapeJavaScript) {
                value = this.escapeScript(value);
            }
            if (escapeSQL) {
                value = this.escapeSql(value);
            }
            setValue(value);
        }
    }

    @Override
    public String getAsText() {
        Object value = getValue();
        return value != null ? value.toString() : "";
    }

    /**
     * 剥离SQL注入部分代码
     * @param value
     * @return
     */
    public String escapeSql(String value) {
        return value.replaceAll("('.+--)|(--)|(\\|)|(%7C)", "");
    }

    /**
     * 剥离js注入
     * @param value
     * @return
     */
    public String escapeScript(String value){
        value = value.replace("script", "\\script").replace("/script", "\\/script");
        return value;
    }
}


package com.sn.framework.core.database;

/**
 * 解决oracle10g方言问题
 * @author wyl
 * @date 2016-11-25
 */

import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.type.StandardBasicTypes;

import java.sql.Types;


public class MyOracleDialect extends Oracle10gDialect {
    public MyOracleDialect() {
        super();
        registerHibernateType(Types.CHAR, StandardBasicTypes.STRING.getName());
        registerHibernateType(Types.NVARCHAR, StandardBasicTypes.STRING.getName());
        registerHibernateType(Types.LONGNVARCHAR, StandardBasicTypes.STRING.getName());
        registerHibernateType(Types.DECIMAL, StandardBasicTypes.DOUBLE.getName());
        registerHibernateType(Types.NCLOB, StandardBasicTypes.STRING.getName());
    }
}
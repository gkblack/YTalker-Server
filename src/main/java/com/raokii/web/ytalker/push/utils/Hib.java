package com.raokii.web.ytalker.push.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Rao
 * @date 2019/2/15
 */
public class Hib {

    // 全局的 SessionFactory
    private static SessionFactory sessionFactory;

    private static final Logger LOGGER = Logger.getLogger(Hib.class.getName());
    /**
     * 获取全局的sessionFactory
     *
     * @return
     */
    public static SessionFactory sessionFactory() {
        return sessionFactory;
    }

    public static void setup() {
        LOGGER.log(Level.INFO, "Hibernate setup succeed!");
    }

    static {
        // 静态初始化sessionFactory
        init();
    }

    public static void init() {
        // 从hibernate.cfg.xml中初始化
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();

        try {
            sessionFactory = new MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            e.getStackTrace();
            // 打印错误并销毁
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    /**
     * 得到session会话
     *
     * @return
     */
    public static Session session() {
        return sessionFactory.getCurrentSession();
    }

    public static void closeFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    public interface QueryOnly {
        void query(Session session);
    }

    /**
     * 仅查询数据，无返回值
     *
     * @param query
     */
    public static void queryOnly(QueryOnly query) {
        // 重新打开一个session
        Session session = sessionFactory.openSession();
        // 开启事物
        final Transaction transaction = session.beginTransaction();

        try {
            // 调用传递进来的接口
            // 调用接口的方法把session传递进去
            query.query(session);
            // 提交
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            // 异常则回滚
            try {
                transaction.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } finally {
            session.close();
        }
    }

    // 用户时间操作的一个接口
    public interface Query<T> {
        T query(Session session);
    }

    // 简化session操作的工具方法,查询数据并返回查询结果
    public static <T> T query(Query<T> query) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        T t = null;

        try {
            t = query.query(session);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                transaction.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } finally {
            session.close();
        }
        return t;
    }
}

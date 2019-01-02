/*
 * Copyright (c)  2017 Hiylo Org. All rights reserved
 * Project: framework
 * File: Dsl.java
 * Data: 11/27/17 7:54 AM
 * Author: hiylo
 */

package org.hiylo.components.db;

import com.vividsolutions.jts.geom.Geometry;

import java.util.*;

/**
 * @author hiylo
 * @date 2015年10月19日 08:08:57
 */
public class Dsl {
    private StringBuffer hql = new StringBuffer();
    /**
     * 要查询的列
     */
    private List<String> fields;
    /**
     * 从什么时间开始
     */
    private Map<String, String> sinceCondition;
    private Map<String, String> equalsCondition;
    private Map<String, String> greaterCondition;
    private Map<String, String> lessCondition;
    private Map<String, String> betweenCondition;
    private Map<String, String> orderCondition;
    private List<String> isTrueCondition;
    private List<String> isFalseCondition;
    private Map<String, String> likeCondition;
    private Map<String, String> orLikeCondition;
    private List<String> isNullCondition;
    private List<String> groupByCondition;
    private String countField;
    private String id;
    private DistanceCondition distanceCondition;
    private Class clazz;
    private List<CompareCondition> compareConditions;
    private JoinCondition joinCondition;
    private Map<String, String> inCondition;

    /**
     * <p>
     * Description: 初始化hql
     * </p>
     */

    public Dsl() {

    }

    public static void main(String[] args) {
        Dsl dsl = new Dsl().select(Dsl.class).in("helperCode", new ArrayList<String>() {
            {
                add("0-983374976956497920-131072-4096-0");
                add("H-029509921955401092e045940a688498-1521711573112");
            }
        }, true, true);
        System.out.println(dsl.generate());
        dsl = new Dsl().select(Dsl.class).in("helperCode", "(1, 2, 3)", true);
        System.out.println(dsl.generate());
    }

    /**
     * @param
     * @return void
     * @throws @author 朱玺
     * @date 2016年12月2日
     */
    private void init() {
    }

    public Dsl distanceNear(Geometry point, String field) {
        distanceCondition = new DistanceCondition(point, field, 0, null);
        return this;
    }

    public Dsl distance(Geometry point, String field, int distance, ConditionType type) {
        distanceCondition = new DistanceCondition(point, field, distance, type);
        return this;
    }

    public Dsl orLike(String field, String value) {
        if (this.orLikeCondition == null) {
            this.orLikeCondition = new HashMap(0, 0.75F);
        }
        orLikeCondition.put(" o." + field, value);
        return this;
    }

    public Dsl select(Class<?> clazz) throws DslException {
        if (clazz == null) {
            throw new DslException("类型不能为null");
        }
        this.clazz = clazz;
        // init();
        return this;
    }

    public Dsl field(String... fields) {
        if (this.fields == null) {
            this.fields = new ArrayList<String>(3);
        }
        for (String f : fields) {
            this.fields.add("o." + f);
        }
        return this;
    }

    public Dsl field(boolean needO, String... fields) {
        if (this.fields == null) {
            this.fields = new ArrayList<String>(3);
        }
        for (String f : fields) {
            this.fields.add(f);
        }
        return this;
    }

    public Dsl field(String field, boolean needO) {
        if (this.fields == null) {
            this.fields = new ArrayList<String>(3);
        }
        if (needO) {
            this.fields.add("o." + field);
        } else {
            this.fields.add(field);
        }
        return this;
    }

    public Dsl between(String field, String begin, String end) {
        if (this.betweenCondition == null) {
            this.betweenCondition = new HashMap(0, 0.75F);
        }
        betweenCondition.put(field, begin + " and " + end);
        return this;
    }

    public Dsl equals(String field, String value, boolean isString) {
        if (this.equalsCondition == null) {
            this.equalsCondition = new HashMap(0, 0.75F);
        }
        if (isString) {
            equalsCondition.put("o." + field, "\'" + value + "\'");
        } else {
            equalsCondition.put("o." + field, value);
        }
        return this;
    }

    public Dsl equals(String field, String value, boolean isString, boolean needO) {
        if (this.equalsCondition == null) {
            this.equalsCondition = new HashMap(0, 0.75F);
        }
        if (needO) {
            field = "o." + field;
        }
        if (isString) {
            equalsCondition.put(field, "\'" + value + "\'");
        } else {
            equalsCondition.put(field, value);
        }
        return this;
    }

    public Dsl compare(String field, String value, boolean isString, ConditionType type, boolean needO) {
        if (compareConditions == null) {
            compareConditions = new ArrayList<CompareCondition>(5);
        }
        compareConditions.add(new CompareCondition(field, value, isString, type, needO));
        return this;
    }

    public Dsl compare(String field, String value, boolean isString, ConditionType type) {
        if (compareConditions == null) {
            compareConditions = new ArrayList<CompareCondition>(5);
        }
        compareConditions.add(new CompareCondition(field, value, isString, type));
        return this;
    }

    public Dsl order(String field, String value, boolean needO) {
        if (this.orderCondition == null) {
            this.orderCondition = new HashMap(0, 0.75F);
        }
        orderCondition.put(needO ? "o." + field : field, value);
        return this;
    }

    public Dsl isTrue(String field) {
        if (this.isTrueCondition == null) {
            this.isTrueCondition = new ArrayList<String>(3);
        }
        isTrueCondition.add(field);
        return this;
    }

    public Dsl isFalse(String field) {
        if (this.isFalseCondition == null) {
            this.isFalseCondition = new ArrayList<String>(3);
        }
        isFalseCondition.add(field);
        return this;
    }

    public Dsl join(String type, String field, String alias) {

        joinCondition = new JoinCondition(type, field, alias);
        return this;
    }

    public Dsl like(String field, String value) {
        if (this.likeCondition == null) {
            this.likeCondition = new HashMap(0, 0.75F);
        }
        likeCondition.put(" o." + field, value);
        return this;
    }

    public Dsl groupby(String field, boolean needO) {
        if (this.groupByCondition == null) {
            this.groupByCondition = new ArrayList<String>(1);
        }
        if (needO) {
            groupByCondition.add("o." + field);
        } else {
            groupByCondition.add(field);
        }
        return this;
    }

    /**
     * 可以以String和数组或者集合形式传入in的参数
     *
     * @param object in 的条件参数, List
     * @return Dsl
     */
    public Dsl in(String field, List object, boolean isString, boolean needO) {
        if (this.inCondition == null) {
            this.inCondition = new HashMap<>(1);
        }

        if (object.size() > 0) {
            StringBuffer value = new StringBuffer();
            value.append("(");
            for (Object obj : object) {
                if (isString) {
                    value.append("'" + obj.toString() + "',");
                } else {
                    value.append(obj.toString() + ",");
                }
            }
            value = new StringBuffer(value.toString().substring(0, value.length() - 1));
            value.append(")");
            if (needO) {
                inCondition.put("o." + field, value.toString());
            } else {
                inCondition.put(field, value.toString());
            }
        }
        return this;
    }

    /**
     * 可以以String形式传入in的参数
     *
     * @param obejct in 的条件参数, 可以是String , 格式(1, 2, 3) 或者 ('1', '2', '3')
     * @return Dsl
     */
    public Dsl in(String field, String obejct, boolean needO) {
        if (this.inCondition == null) {
            this.inCondition = new HashMap<>(1);
            if (obejct instanceof String) {
                if (needO) {
                    inCondition.put("o." + field, obejct.toString());
                } else {
                    inCondition.put(field, obejct.toString());
                }
            }
        }
        return this;
    }
    // select
    // name,st_distance(point(113.327955,23.129717),point)*111195
    // as distance,address from table1 where
    // st_distance(point(113.327955,
    // 23.129717),point)*111195 < 100 order by distance asc limit 100

    public String generate() {
        String content = null;
        boolean hasCondition = false;
        if (distanceCondition != null) {
            content = distanceCondition.getPoint().toString();
            String[] ss = content.split(" ");
            content = ss[0] + ss[1] + ", " + ss[2];
        }
        if (fields != null && fields.size() > 0) {
            if (countField == null) {
                hql.append("select " + listToStringSplitViaDot(fields));
            } else {
                hql.append("select " + listToStringSplitViaDot(fields) + ", count(" + countField + ")");
            }
            hasCondition = true;
        } else if (countField != null) {
            hql.append("select count(" + countField + ")");
            hasCondition = true;
        }
        if (distanceCondition != null) {
            if (hasCondition) {
                hql.append(", st_distance(RBOUNDS,geomFromText(' " + content + ", " + distanceCondition.getField() + "'))"
                        + " as distance ");
            } else {
                hql.append("select st_distance(RBOUNDS,geomFromText(' " + content + ", " + distanceCondition.getField()
                        + "'))" + " as distance ");
                hasCondition = true;
            }
        }
        if (hasCondition) {
            hql.append(" from " + clazz.getName() + " o ");
        } else {
            hql = new StringBuffer("from " + clazz.getName() + " o ");
        }
        if (joinCondition != null) {
            hql.append(" " + joinCondition.getType() + " join " + joinCondition.getField() + " "
                    + joinCondition.getAlias());
        }
        boolean hasNotWhere = true;
        if (id != null && !"".equals(id)) {
            if (hasNotWhere) {
                hql.append(" where ");
                hasNotWhere = false;
            } else {
                hql.append(" and ");
            }
            hql.append(" o.id = " + id);
        }
        if (betweenCondition != null && betweenCondition.size() > 0) {
            Iterator<String> keys = betweenCondition.keySet().iterator();
            while (keys.hasNext()) {
                if (hasNotWhere) {
                    hql.append(" where ");
                    hasNotWhere = false;
                } else {
                    hql.append(" and ");
                }
                String key = keys.next();
                hql.append(" o." + key + " between " + betweenCondition.get(key) + " ");
            }
        }
        if (equalsCondition != null && equalsCondition.size() > 0) {
            Iterator<String> keys = equalsCondition.keySet().iterator();
            while (keys.hasNext()) {
                if (hasNotWhere) {
                    hql.append(" where ");
                    hasNotWhere = false;
                } else {
                    hql.append(" and ");
                }
                String key = keys.next();
                hql.append(key + " = " + equalsCondition.get(key));
            }
        }
        if (isTrueCondition != null && isTrueCondition.size() > 0) {
            if (hasNotWhere) {
                hql.append(" where ");
                hasNotWhere = false;
            } else {
                hql.append(" and ");
            }
            for (String key : isTrueCondition) {
                hql.append(" o." + key + " = true");
            }
        }
        if (isFalseCondition != null && isFalseCondition.size() > 0) {
            if (hasNotWhere) {
                hql.append(" where ");
                hasNotWhere = false;
            } else {
                hql.append(" and ");
            }
            for (String key : isFalseCondition) {
                hql.append(" o." + key + " = false");
                // is true is false
            }
        }
        if (likeCondition != null && likeCondition.size() > 0) {
            Iterator<String> keys = likeCondition.keySet().iterator();
            while (keys.hasNext()) {
                if (hasNotWhere) {
                    hql.append(" where ");
                    hasNotWhere = false;
                } else {
                    hql.append(" and ");
                }
                String key = keys.next();
                hql.append(key + " like '%" + likeCondition.get(key) + "%'");
            }
        }
        if (orLikeCondition != null && orLikeCondition.size() > 0) {
            Iterator<String> keys = orLikeCondition.keySet().iterator();
            while (keys.hasNext()) {
                if (hasNotWhere) {
                    hql.append(" where ");
                    hasNotWhere = false;
                } else {
                    hql.append(" or ");
                }
                String key = keys.next();
                hql.append(key + " like '%" + orLikeCondition.get(key) + "%'");
            }
        }
        if (isNullCondition != null && isNullCondition.size() > 0) {
            for (String field : isNullCondition) {
                if (hasNotWhere) {
                    hql.append(" where ");
                    hasNotWhere = false;
                } else {
                    hql.append(" and ");
                }
                hql.append(" o." + field + " is null");
            }
        }
        if (inCondition != null && inCondition.size() > 0) {
            Iterator<String> keys = inCondition.keySet().iterator();
            while (keys.hasNext()) {
                if (hasNotWhere) {
                    hql.append(" where ");
                    hasNotWhere = false;
                } else {
                    hql.append(" and ");
                }
                String key = keys.next();
                hql.append(key + " in " + inCondition.get(key) + " ");
            }
        }
        if (compareConditions != null && compareConditions.size() > 0) {
            for (CompareCondition compareCondition : compareConditions) {
                if (hasNotWhere) {
                    hql.append(" where ");
                    hasNotWhere = false;
                } else {
                    hql.append(" and ");
                }
                if (compareCondition.getType().equals(ConditionType.GREATER)) {
                    if (compareCondition.isNeedO()) {
                        if (compareCondition.isString()) {
                            hql.append(" o." + compareCondition.getField() + " > '" + compareCondition.getValue() + "'");
                        } else {
                            hql.append(" o." + compareCondition.getField() + " > " + compareCondition.getValue());
                        }
                    } else {
                        if (compareCondition.isString()) {
                            hql.append(compareCondition.getField() + " > '" + compareCondition.getValue() + "'");
                        } else {
                            hql.append(compareCondition.getField() + " > " + compareCondition.getValue());
                        }
                    }
                } else if (compareCondition.getType().equals(ConditionType.LESS)) {
                    if (compareCondition.isNeedO()) {
                        if (compareCondition.isString()) {
                            hql.append(" o." + compareCondition.getField() + " < '" + compareCondition.getValue() + "'");
                        } else {
                            hql.append(" o." + compareCondition.getField() + " < " + compareCondition.getValue());
                        }
                    } else {
                        if (compareCondition.isString()) {
                            hql.append(compareCondition.getField() + " < '" + compareCondition.getValue() + "'");
                        } else {
                            hql.append(compareCondition.getField() + " < " + compareCondition.getValue());
                        }
                    }
                } else if (compareCondition.getType().equals(ConditionType.LESSEQUES)) {
                    if (compareCondition.isNeedO()) {
                        if (compareCondition.isString()) {
                            hql.append(" o." + compareCondition.getField() + " <= '" + compareCondition.getValue() + "'");
                        } else {
                            hql.append(" o." + compareCondition.getField() + " <= " + compareCondition.getValue());
                        }
                    } else {
                        if (compareCondition.isString()) {
                            hql.append(compareCondition.getField() + " <= '" + compareCondition.getValue() + "'");
                        } else {
                            hql.append(compareCondition.getField() + " <= " + compareCondition.getValue());
                        }
                    }
                } else if (compareCondition.getType().equals(ConditionType.GREATEEQUES)) {
                    if (compareCondition.isNeedO()) {
                        if (compareCondition.isString()) {
                            hql.append(" o." + compareCondition.getField() + " >= '" + compareCondition.getValue() + "'");
                        } else {
                            hql.append(" o." + compareCondition.getField() + " >= " + compareCondition.getValue());
                        }
                    } else {
                        if (compareCondition.isString()) {
                            hql.append(compareCondition.getField() + " >= '" + compareCondition.getValue() + "'");
                        } else {
                            hql.append(compareCondition.getField() + " >= " + compareCondition.getValue());
                        }
                    }
                } else if (compareCondition.getType().equals(ConditionType.UNEQUALS)) {
                    if (compareCondition.isNeedO()) {
                        if (compareCondition.isString()) {
                            hql.append(" o." + compareCondition.getField() + " <> '" + compareCondition.getValue() + "'");
                        } else {
                            hql.append(" o." + compareCondition.getField() + " <> " + compareCondition.getValue());
                        }
                    } else {
                        if (compareCondition.isString()) {
                            hql.append(compareCondition.getField() + " <> '" + compareCondition.getValue() + "'");
                        } else {
                            hql.append(compareCondition.getField() + " <> " + compareCondition.getValue());
                        }
                    }
                }
            }
        }
        if (groupByCondition != null && groupByCondition.size() > 0) {
            int index = 0;
            for (String condition : groupByCondition) {
                index++;
                if (index == 1) {
                    hql.append(" group by " + condition);
                } else {
                    hql.append("," + condition);
                }
            }
        }
        if (orderCondition != null && orderCondition.size() > 0) {
            Iterator<String> keys = orderCondition.keySet().iterator();
            int index = 0;
            while (keys.hasNext()) {
                index++;
                String key = keys.next();
                if (index == 1) {
                    hql.append(" order by " + key + " " + orderCondition.get(key) + "");
                } else {
                    hql.append(", " + key + " " + orderCondition.get(key) + "");
                }
            }
        }
        return hql.toString();
    }

    public Dsl count(String field) {
        this.countField = field;
        return this;
    }

    public Dsl viaId(String id) {
        this.id = id;
        return this;
    }

    public Dsl isNull(String field) {
        if (isNullCondition == null) {
            isNullCondition = new ArrayList<String>(3);
        }
        isNullCondition.add(field);
        return this;
    }

    private String listToStringSplitViaDot(List<String> ss) {
        StringBuffer sb = new StringBuffer("");
        int index = 0;
        int count = ss.size();
        for (String s : ss) {
            index++;
            sb.append(s);
            if (index < count) {
                sb.append(", ");
            }
        }
        sb.append(" ");
        return sb.toString();
    }

    public enum ConditionType {
        //时间判断,什么时候开始
        SINCE,
        //between
        BETWEEN,
        // 等于
        EQUALS,
        //大于
        GREATER,
        // 小于
        LESS,
        UNEQUALS,
        ORDER,
        //小于等于
        LESSEQUES,
        //大于等于
        GREATEEQUES;
    }
}

class DistanceCondition {
    private Geometry point;
    private String field;
    private int distance;
    private Dsl.ConditionType type;

    public DistanceCondition() {
        super();
    }

    public DistanceCondition(Geometry point, String field, int distance, Dsl.ConditionType type) {
        super();
        this.point = point;
        this.field = field;
        this.distance = distance;
        this.type = type;
    }

    public Geometry getPoint() {
        return point;
    }

    public void setPoint(Geometry point) {
        this.point = point;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Dsl.ConditionType getType() {
        return type;
    }

    public void setType(Dsl.ConditionType type) {
        this.type = type;
    }

}

class CompareCondition {
    private String field;
    private String value;
    private boolean isString;
    private Dsl.ConditionType type;
    private boolean needO;

    public CompareCondition(String field, String value, boolean isString, Dsl.ConditionType type, boolean needO) {
        super();
        this.field = field;
        this.value = value;
        this.isString = isString;
        this.type = type;
        this.needO = needO;
    }

    public CompareCondition(String field, String value, boolean isString, Dsl.ConditionType type) {
        super();
        this.field = field;
        this.value = value;
        this.isString = isString;
        this.type = type;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isString() {
        return isString;
    }

    public void setString(boolean isString) {
        this.isString = isString;
    }

    public Dsl.ConditionType getType() {
        return type;
    }

    public void setType(Dsl.ConditionType type) {
        this.type = type;
    }

    public boolean isNeedO() {
        return needO;
    }

    public void setNeedO(boolean needO) {
        this.needO = needO;
    }
}

class JoinCondition {
    private String type;
    private String field;
    private String alias;

    public JoinCondition(String type, String field, String alias) {
        super();
        this.type = type;
        this.field = field;
        this.alias = alias;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

}
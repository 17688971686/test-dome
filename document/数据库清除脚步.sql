--专家
ALTER TABLE CS_EXPERT
 DROP PRIMARY KEY CASCADE;

DROP TABLE CS_EXPERT CASCADE CONSTRAINTS;

--专家评审方案
ALTER TABLE CS_EXPERT_REVIEW
 DROP PRIMARY KEY CASCADE;

DROP TABLE CS_EXPERT_REVIEW CASCADE CONSTRAINTS;

--专家抽取条件
ALTER TABLE CS_EXPERT_CONDITION
 DROP PRIMARY KEY CASCADE;

DROP TABLE CS_EXPERT_CONDITION CASCADE CONSTRAINTS;

--专家随机选择
ALTER TABLE CS_EXPERT_SELECTED
 DROP PRIMARY KEY CASCADE;

DROP TABLE CS_EXPERT_SELECTED CASCADE CONSTRAINTS;


--专家类型
ALTER TABLE CS_EXPERT_SELECTED
 DROP PRIMARY KEY CASCADE;

DROP TABLE CS_EXPERT_SELECTED CASCADE CONSTRAINTS;


--专家荣誉记录
ALTER TABLE CS_EXPERT_GLORY
 DROP PRIMARY KEY CASCADE;

DROP TABLE CS_EXPERT_GLORY CASCADE CONSTRAINTS;

--专家聘书
ALTER TABLE CS_EXPERT_OFFER
 DROP PRIMARY KEY CASCADE;

DROP TABLE CS_EXPERT_OFFER CASCADE CONSTRAINTS;

--项目经验
ALTER TABLE CS_PROJECT_EXPE
 DROP PRIMARY KEY CASCADE;

DROP TABLE CS_PROJECT_EXPE CASCADE CONSTRAINTS;

--专家 工作简历表
ALTER TABLE CS_WORKE_EXPE
 DROP PRIMARY KEY CASCADE;
DROP TABLE CS_WORKE_EXPE CASCADE CONSTRAINTS;

--协审计划表
ALTER TABLE CS_AS_PLAN
 DROP PRIMARY KEY CASCADE;
DROP TABLE CS_AS_PLAN CASCADE CONSTRAINTS;

--协审计划表
ALTER TABLE CS_AS_PLAN
 DROP PRIMARY KEY CASCADE;
DROP TABLE CS_AS_PLAN CASCADE CONSTRAINTS;


--协审项目
ALTER TABLE CS_AS_PLANSIGN
 DROP PRIMARY KEY CASCADE;

DROP TABLE CS_AS_PLANSIGN CASCADE CONSTRAINTS;



--协审计划项目管理表
DROP TABLE CS_AS_PLAN_CS_AS_PLANSIGN CASCADE CONSTRAINTS;

--协审单位
ALTER TABLE CS_AS_UNIT
 DROP PRIMARY KEY CASCADE;

DROP TABLE CS_AS_UNIT CASCADE CONSTRAINTS;

--协审计划协审单位
DROP TABLE CS_AS_PLAN_CS_AS_UNIT CASCADE CONSTRAINTS;

--协审单位人员
DROP TABLE CS_AS_UNITUSER CASCADE CONSTRAINTS;


--项目关联
ALTER TABLE CS_ASSOCIATE_SIGN
 DROP PRIMARY KEY CASCADE;

DROP TABLE CS_ASSOCIATE_SIGN CASCADE CONSTRAINTS;

--发文
ALTER TABLE CS_DISPATCH_DOC
 DROP PRIMARY KEY CASCADE;

DROP TABLE CS_DISPATCH_DOC CASCADE CONSTRAINTS;


--发文
ALTER TABLE CS_DISPATCH_DOC
 DROP PRIMARY KEY CASCADE;

DROP TABLE CS_DISPATCH_DOC CASCADE CONSTRAINTS;


--合并发文
ALTER TABLE CS_MERGE_DISPA
 DROP PRIMARY KEY CASCADE;

DROP TABLE CS_MERGE_DISPA CASCADE CONSTRAINTS;

--归档信息
ALTER TABLE CS_FILE_RECORD
 DROP PRIMARY KEY CASCADE;

DROP TABLE CS_FILE_RECORD CASCADE CONSTRAINTS;


--会议室
ALTER TABLE CS_MEETING_ROOM
 DROP PRIMARY KEY CASCADE;

DROP TABLE CS_MEETING_ROOM CASCADE CONSTRAINTS;

--会议室预定
ALTER TABLE CS_ROOM_BOOKING
 DROP PRIMARY KEY CASCADE;

DROP TABLE CS_ROOM_BOOKING CASCADE CONSTRAINTS;

--收文负责人
ALTER TABLE CS_SIGN_PRINCIPAL
 DROP PRIMARY KEY CASCADE;

DROP TABLE CS_SIGN_PRINCIPAL CASCADE CONSTRAINTS;

--收文
ALTER TABLE CS_SIGN
 DROP PRIMARY KEY CASCADE;

DROP TABLE CS_SIGN CASCADE CONSTRAINTS;

--附件
ALTER TABLE CS_SYSFILE
 DROP PRIMARY KEY CASCADE;

DROP TABLE CS_SYSFILE CASCADE CONSTRAINTS;


--工作方案
ALTER TABLE CS_WORK_PROGRAM
 DROP PRIMARY KEY CASCADE;

DROP TABLE CS_WORK_PROGRAM CASCADE CONSTRAINTS;

--清除流程数据
delete from ACT_GE_BYTEARRAY;
delete from ACT_HI_ACTINST;
delete from ACT_HI_COMMENT;
delete from ACT_HI_IDENTITYLINK;
delete from ACT_HI_PROCINST;
delete from ACT_HI_TASKINST;
delete from ACT_HI_VARINST;
delete from ACT_RE_DEPLOYMENT;

delete from ACT_RU_IDENTITYLINK;
delete from ACT_RU_JOB;
delete from ACT_RU_TASK;
delete from ACT_RU_VARIABLE;
delete from ACT_RE_PROCDEF;
delete from ACT_RU_EXECUTION;
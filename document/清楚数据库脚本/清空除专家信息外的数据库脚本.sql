/************** 清除数据脚本*************************/
/*一、专家评审方案部分*/
delete from CS_EXPERT_CONDITION;
delete from CS_EXPERT_SELECTED;
delete from CS_EXPERT_REVIEW;
/*三、会议室部分*/
delete from CS_MEETING_ROOM;
delete from CS_ROOM_BOOKING;
/*四、财务部分*/
delete from cs_financial_manager;
/*五、附件部分*/
delete from cs_sysfile;
/*六、补充资料函和补充资料月报简报*/
delete from cs_add_suppLetter;
delete from cs_add_registerfile;
delete from cs_monthly_newsletter;
delete from CS_ADD_REGISTERFILE;
delete from CS_ADD_SUPPLETTER;
/*七、项目协审*/
delete from cs_as_plansign;
delete from CS_AS_PLAN_CS_AS_UNIT;
delete from CS_AS_PLAN_CS_AS_UNIT2;
delete from CS_AS_PLANSIGN;
delete from CS_ASSOCIATE_SIGN;
delete from cs_as_plan;
/*八、项目部分*/
delete from cs_projectStop;
delete from cs_dispatch_doc;
delete from cs_work_program;
delete from cs_file_record;
delete from SIGN_DISP_WORK;
delete from cs_sign;
/*其它*/
delete from cs_sign_branch;
delete from cs_sign_principal2;
delete from cs_archives_Library;
delete from cs_annountment;

/*删除流程数据*/
delete from ACT_HI_ACTINST;
delete from ACT_HI_COMMENT;
delete from ACT_HI_IDENTITYLINK;
delete from ACT_HI_PROCINST;
delete from ACT_HI_TASKINST;
delete from ACT_HI_VARINST;
delete from ACT_RU_IDENTITYLINK;
delete from ACT_RU_TASK;
delete from ACT_RU_VARIABLE;
delete from ACT_RU_EXECUTION;
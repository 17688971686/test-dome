/************** 清除数据脚本*************************/
/*一、专家评审方案部分*/
drop table CS_EXPERT_COUNT;
delete from CS_EXPERT_CONDITION;
delete from CS_EXPERT_SELECTED;
delete from CS_EXPERT_REVIEW;
/*二、专家部分*/
delete from CS_EXPERT_GLORY;
delete from CS_EXPERT_OFFER;
delete from CS_EXPERT_TYPE;
delete from cs_project_expe;
delete from cs_worke_expe;
delete from CS_EXPERT;
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
/*七、项目协审*/
delete from cs_as_plansign;
delete from cs_as_plan;
/*八、项目部分*/
delete from cs_projectStop;
delete from cs_dispatch_doc;
delete from cs_work_program;
delete from cs_file_record;
delete from cs_sign;
/*其它*/
delete from cs_sign_branch;
delete from cs_sign_principal2;
delete from cs_archives_Library;
delete from cs_annountment;
delete from cs_sign_principal;

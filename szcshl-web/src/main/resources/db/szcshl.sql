
---------------------专家评审费历史视图-----------------------------------------------------------
CREATE OR REPLACE VIEW V_EXPERT_PAY_HIS AS
select es.id esId,to_char(er.paydate,'yy-mm') payDate,es.expertid,es.reviewcost
from CS_EXPERT_SELECTED es,CS_EXPERT_REVIEW er where es.Expertreviewid = er.id and es.isjoin=9;
-----------------------------------------------------------------------------------------------

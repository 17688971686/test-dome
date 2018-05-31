/* Formatted on 2018/4/10 23:17:16 (QP5 v5.256.13226.35538) */
/*1���ҳ�����Ǩ�Ƶ���Ŀ����*/

SELECT *
  FROM cs_sign cs
 WHERE CS.OLDPROJECTID IS NULL;

/*2��ɾ������ר�Ҳ�������*/

DELETE FROM CS_EXPERT_CONDITION cec
      WHERE CEC.EXPERTREVIEWID IN (SELECT CER.ID
                                     FROM CS_EXPERT_REVIEW cer
                                    WHERE CER.BUSINESSID IN (SELECT CS.SIGNID
                                                               FROM cs_sign cs
                                                              WHERE CS.OLDPROJECTID
                                                                       IS  NULL));

DELETE FROM CS_EXPERT_SELECTED ces
      WHERE ces.EXPERTREVIEWID IN (SELECT CER.ID
                                     FROM CS_EXPERT_REVIEW cer
                                    WHERE CER.BUSINESSID IN (SELECT CS.SIGNID
                                                               FROM cs_sign cs
                                                              WHERE CS.OLDPROJECTID
                                                                       IS NULL));

DELETE FROM CS_EXPERT_REVIEW cer
      WHERE CER.BUSINESSID IN (SELECT CS.SIGNID
                                 FROM cs_sign cs
                                WHERE CS.OLDPROJECTID IS NULL);

/*3��ɾ��������Ԥ����Ϣ*/

DELETE FROM CS_ROOM_BOOKING crb
      WHERE CRB.BUSINESSID IN (SELECT CWP.ID
                                 FROM CS_WORK_PROGRAM cwp
                                WHERE CWP.SIGNID IN (SELECT CS.SIGNID
                                                       FROM cs_sign cs
                                                      WHERE CS.OLDPROJECTID
                                                               IS NULL));

/*4��ɾ����������*/

DELETE FROM cs_financial_manager cfm
      WHERE CFM.BUSINESSID IN (SELECT CS.SIGNID
                                 FROM cs_sign cs
                                WHERE CS.OLDPROJECTID IS NULL);

/*5��ɾ��������Ϣ*/

DELETE FROM cs_sysfile csf
      WHERE CSF.BUSINESSID IN (SELECT CS.SIGNID
                                 FROM cs_sign cs
                                WHERE CS.OLDPROJECTID IS NULL);

/*6���������Ϻ��Ͳ��������±���*/

DELETE FROM CS_ADD_SUPPLETTER cas
      WHERE cas.BUSINESSID IN (SELECT CS.SIGNID
                                 FROM cs_sign cs
                                WHERE CS.OLDPROJECTID IS NULL);

DELETE FROM CS_ADD_REGISTERFILE car
      WHERE car.BUSINESSID IN (SELECT CS.SIGNID
                                 FROM cs_sign cs
                                WHERE CS.OLDPROJECTID IS NULL);

/*7��ɾ����֧����Ŀ������*/

DELETE FROM cs_sign_branch csb
      WHERE CSB.SIGNID IN (SELECT CS.SIGNID
                             FROM cs_sign cs
                            WHERE CS.OLDPROJECTID IS NULL);

DELETE FROM cs_sign_principal2 csp2
      WHERE CSP2.SIGNID IN (SELECT CS.SIGNID
                              FROM cs_sign cs
                             WHERE CS.OLDPROJECTID IS NULL);

/*8��ɾ����Ŀ��Ϣ*/

DELETE FROM cs_projectStop cps
      WHERE CPS.SIGNID IN (SELECT CS.SIGNID
                             FROM cs_sign cs
                            WHERE CS.OLDPROJECTID IS NULL);

DELETE FROM cs_appraise_report car
      WHERE CAR.SIGNID IN (SELECT CS.SIGNID
                             FROM cs_sign cs
                            WHERE CS.OLDPROJECTID IS NULL);

DELETE FROM cs_dispatch_doc cdd
      WHERE CDD.SIGNID IN (SELECT CS.SIGNID
                             FROM cs_sign cs
                            WHERE CS.OLDPROJECTID IS NULL);

DELETE FROM cs_work_program cwp
      WHERE cwp.SIGNID IN (SELECT CS.SIGNID
                             FROM cs_sign cs
                            WHERE CS.OLDPROJECTID IS NULL);

DELETE FROM cs_file_record cfr
      WHERE cfr.SIGNID IN (SELECT CS.SIGNID
                             FROM cs_sign cs
                            WHERE CS.OLDPROJECTID IS NULL);

DELETE FROM CS_ASSOCIATE_SIGN cas
      WHERE    CAS.ASSOCIATE_SIGNID IN (SELECT CS.SIGNID
                                          FROM cs_sign cs
                                         WHERE CS.OLDPROJECTID IS NULL)
            OR CAS.SIGNID IN (SELECT CS.SIGNID
                                FROM cs_sign cs
                               WHERE CS.OLDPROJECTID IS NULL);

DELETE FROM cs_sign cs
      WHERE CS.OLDPROJECTID IS NULL;

DELETE FROM SIGN_DISP_WORK sdw
WHERE sdw.OLDPROJECTID IS NULL;
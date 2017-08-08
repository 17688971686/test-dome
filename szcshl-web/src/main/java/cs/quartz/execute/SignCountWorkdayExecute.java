package cs.quartz.execute;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import cs.common.Constant;
import cs.domain.project.ProjectStop;
import cs.domain.project.Sign;
import cs.quartz.unit.QuartzUnit;
import cs.service.project.ProjectStopService;
import cs.service.project.SignService;

/**
 * 计算工作日的执行类
 *
 * @author MCL
 * @date 2017年7月4日 下午4:41:43
 */
@Controller
public class SignCountWorkdayExecute implements Job {

	@Autowired
	private SignService signService;

	@Autowired
	private ProjectStopService projectStopService;

	/*警示灯状态如下：
	 * PROCESS("1"),	//在办
	 * DISPA("2"),		//已发文
	 * ARCHIVE("3"),	//已发送存档
	 * PAUSE("4"),		//暂停
	 * UNDER3WORKDAY("5"),		//少于三个工作日
	 * DISPAOVER("6"),			//发文超期
	 * OVER25WORKDAYARCHIVE("7"),	//超过25个工作日未存档
	 * ARCHIVEOVER("8");		//存档超期
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		QuartzUnit unit = new QuartzUnit();
		// 1、查询正在办理的项目，通过签收时间计算工作日，通过项目类型判断超过多少个工作日后显示警示灯，不同阶段显示不同的警示灯
		List<Sign> signList = signService.selectSignNotFinish();
		for (Sign sign : signList) {
			sign.setIsLightUp(Constant.signEnumState.PROCESS.getValue()); //设置 默认状态（在办）

		// 2、通过收文ID查找 项目暂停情况,并计算项目总共暂停了几个工作日
			List<ProjectStop> projectStopList=projectStopService.findProjectStopBySign(sign.getSignid());
			float stopWorkday=0;	//记录总共暂停的工作日
			for(ProjectStop ps : projectStopList){
				stopWorkday += ps.getPausedays();
			}
		//3、判断该项目是否为暂停状态。 未暂停，计算从正式签收到当前时间的工作日，再减掉暂停的工作日，并设置相对应的状态
			if (!Constant.EnumState.STOP.getValue().equals(sign.getSignState())) { 
//				sign.setIsLightUp(Constant.signEnumState.PAUSE.getValue());
//			}else{
				sign.setSurplusdays(sign.getSurplusdays() - 1);     //剩余工作日减一

				float count = unit.countWorkday(sign.getSigndate())-stopWorkday;

				String reviewstage = sign.getReviewstage();//获取评审阶段

				// 已发文状态
				if (sign.getProcessState() == Constant.SignProcessState.END_DIS.getValue()) {   
					sign.setIsLightUp(Constant.signEnumState.DISPA.getValue());
				}

				// 已发送存档状态
				if (sign.getIsSendFileRecord() == Constant.EnumState.YES.getValue() && sign.getFilenum().isEmpty()) { 
					sign.setIsLightUp(Constant.signEnumState.ARCHIVE.getValue());
				}
				// 通过不同的项目阶段，判断，设置状态
				switch (reviewstage) {
				case "项目建议书":
					if (count >= 12) {
						if (sign.getProcessState() == Constant.SignProcessState.END_DIS.getValue()) {    //已发文
							if (count > 25) {
								sign.setIsLightUp(Constant.signEnumState.OVER25WORKDAYARCHIVE.getValue());    //超过25个工作日未存档
							} else {
								sign.setIsLightUp(Constant.signEnumState.ARCHIVEOVER.getValue());    //存档超期
							}
						} else {        //未发文
							sign.setIsLightUp(Constant.signEnumState.DISPAOVER.getValue());    //发文超期
						}
					} else {
						if (12 - count <= 3) {    //少于3个工作日
							sign.setIsLightUp(Constant.signEnumState.UNDER3WORKDAY.getValue());
						}
					}


					break;

				case "可行性研究报告":
					if (count >= 15) {
						if (sign.getProcessState() == Constant.SignProcessState.END_DIS.getValue()) {    //已发文
							if (count > 25) {
								sign.setIsLightUp(Constant.signEnumState.OVER25WORKDAYARCHIVE.getValue());    //超过25个工作日未存档
							} else {
								sign.setIsLightUp(Constant.signEnumState.ARCHIVEOVER.getValue());    //存档超期
							}
						} else {        //未发文
							sign.setIsLightUp(Constant.signEnumState.DISPAOVER.getValue());    //发文超期
						}
					} else {
						if (15 - count <= 3) {    //少于3个工作日
							sign.setIsLightUp(Constant.signEnumState.UNDER3WORKDAY.getValue());
						}
					}
					break;

				case "项目概算":
					if (count >= 15) {
						if (sign.getProcessState() == Constant.SignProcessState.END_DIS.getValue()) {    //已发文
							if (count > 25) {
								sign.setIsLightUp(Constant.signEnumState.OVER25WORKDAYARCHIVE.getValue());    //超过25个工作日未存档
							} else {
								sign.setIsLightUp(Constant.signEnumState.ARCHIVEOVER.getValue());    //存档超期
							}
						} else {        //未发文
							sign.setIsLightUp(Constant.signEnumState.DISPAOVER.getValue());    //发文超期
						}
					} else {
						if (15 - count <= 3) {    //少于3个工作日
							sign.setIsLightUp(Constant.signEnumState.UNDER3WORKDAY.getValue());
						}
					}
					break;

				case "资金申请报告":
					if (count >= 12) {
						if (sign.getProcessState() == Constant.SignProcessState.END_DIS.getValue()) {    //已发文
							if (count > 25) {
								sign.setIsLightUp(Constant.signEnumState.OVER25WORKDAYARCHIVE.getValue());    //超过25个工作日未存档
							} else {
								sign.setIsLightUp(Constant.signEnumState.ARCHIVEOVER.getValue());    //存档超期
							}
						} else {        //未发文
							sign.setIsLightUp(Constant.signEnumState.DISPAOVER.getValue());    //发文超期
						}
					} else {
						if (12 - count <= 3) {    //少于3个工作日
							sign.setIsLightUp(Constant.signEnumState.UNDER3WORKDAY.getValue());
						}
					}
					break;

				case "设备清单（国产）":
					if (count >= 12 || 12 - count <= 3) {
						if (sign.getProcessState() == Constant.SignProcessState.END_DIS.getValue()) {    //已发文
							if (count > 25) {
								sign.setIsLightUp(Constant.signEnumState.OVER25WORKDAYARCHIVE.getValue());    //超过25个工作日未存档
							} else {
								sign.setIsLightUp(Constant.signEnumState.ARCHIVEOVER.getValue());    //存档超期
							}
						} else {        //未发文
							sign.setIsLightUp(Constant.signEnumState.DISPAOVER.getValue());    //发文超期
						}
					} else {
						if (12 - count <= 3) {    //少于3个工作日
							sign.setIsLightUp(Constant.signEnumState.UNDER3WORKDAY.getValue());
						}
					}
					break;

				case "设备清单（进产）":
					if (count >= 12) {
						if (sign.getProcessState() == Constant.SignProcessState.END_DIS.getValue()) {    //已发文
							if (count > 25) {
								sign.setIsLightUp(Constant.signEnumState.OVER25WORKDAYARCHIVE.getValue());    //超过25个工作日未存档
							} else {
								sign.setIsLightUp(Constant.signEnumState.ARCHIVEOVER.getValue());    //存档超期
							}
						} else {        //未发文
							sign.setIsLightUp(Constant.signEnumState.DISPAOVER.getValue());    //发文超期
						}
					} else {
						if (12 - count <= 3) {    //少于3个工作日
							sign.setIsLightUp(Constant.signEnumState.UNDER3WORKDAY.getValue());
						}
					}

					break;
				}
			}

		}
		signService.bathUpdate(signList);
	}

}

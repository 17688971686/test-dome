package cs.ahelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("helper")
public class HelperController {
	@Autowired
	private HelperService helperService;
	@RequestMapping("dataInit")	
	public void DataInit() {
		helperService.DataInit();
	}
	@RequestMapping("delete")	
	public void delete() {
		helperService.delete();
	}
}

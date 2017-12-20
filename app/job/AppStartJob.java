package job;

import java.io.FileNotFoundException;

import play.Logger;
import play.cache.Cache;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.jobs.OnApplicationStop;
import util.InitialServlet;

/**
 * 项目启动时的任务
 *
 */
@OnApplicationStart
public class AppStartJob extends Job{

    public void loadMysqlInfo() throws FileNotFoundException{
    	InitialServlet.initConfig();
        Logger.info("Mysql加载完毕");
    }    
    
    public void doJob() {
        try {
			loadMysqlInfo();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

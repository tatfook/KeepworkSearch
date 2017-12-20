package job;

import play.Logger;
import play.cache.Cache;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.jobs.OnApplicationStop;


@OnApplicationStop
public class AppStopJob extends Job{

    public void doActualJob(){
        Cache.clear();
        Logger.info("清除缓存完毕");
        Cache.stop();
        Logger.info("缓存关闭");
    }
    
    public void doJob() {
    	doActualJob();
    }
}

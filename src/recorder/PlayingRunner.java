package recorder;

import java.io.File;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

public class PlayingRunner{	
	private Executor mExecutor;
	private Logger mLogger;
	
	private Player[] mPlayers;
	private int mPeriodMs;
	
	/**
	 * 
	 * @param logger is the logger where the errors would be logged
	 * @param period Has to be equal to the period set in Recorder for the autonomous to work
	 * @param players I don't like this
	 * @param Hi Tom, writing doc sucks!
	 * @param executor executor for the threads
	 */
    public PlayingRunner(Logger logger, Executor executor, int period, Player... players) {    
        mPlayers = players;
        mPeriodMs = period;
        mLogger = logger;
        mExecutor = executor;
    }
    
    public PlayingRunner(int periodMs, Player... players) {    
    	this(Logger.getLogger("Console.logger"),new Executor(),periodMs,players);
    	ConsoleHandler handler = new ConsoleHandler();
    	mLogger.addHandler(handler);
    }
    
    public PlayingRunner(Logger logger, int period, Player... players) {    
        this(logger,new Executor(),period,players);
    }
    
    public PlayingRunner(Executor executor,int period, Player... players) {    
        this(null,executor,period,players);
    }
    /**
     * @param inputFolder - filename should not include the ".rec"
     */
    public void play(File inputFolder) {
    	if(RecordUtil.ensureIsDirectory(inputFolder) && isFinished()) {
    		for (Player actor : mPlayers) {
                String path = String.format("%s%s.rec", inputFolder.getPath(),actor.getName());
                PlayingTask scriptReader = new PlayingTask(actor, path, mPeriodMs, mLogger);
                mExecutor.submit(scriptReader);
            }
    		mExecutor.stop();
    	}
    }
    
    public boolean isFinished() {
    	return mExecutor.isFinished();
    }
    
    public void stop() {
    	mExecutor.stop();
    }
}

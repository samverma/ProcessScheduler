//Sam Verma
//Process Scheduler - FCFS.java
import java.util.Queue;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class FCFS extends ProcessScheduler
{
    @Override
    public Queue<Process> schedule(PriorityQueue<Process> q) 
    {
    	//Current time slice
        int startTime = 0;
        int queueSize = q.size();
        int finishTime = 0;
        Process p;
        Process scheduled;
        Stats stats = this.getStats();
        Queue<Process> scheduledQueue = new LinkedList<>();
        
        for (int i = 0; i < queueSize; ++i)
        {
        	//Have to poll seperately 
            p = q.poll();
            startTime = Math.max((int) Math.ceil(p.getArrivalTime()), finishTime);            
            finishTime = startTime + p.getBurstTime();
            //Don't start any processes after 100 time slices
            if (startTime > 100) break;
            // Record the stats for this process
            stats.addWaitTime(startTime - p.getArrivalTime());
            stats.addTurnaroundTime(finishTime - p.getArrivalTime());
            stats.addResponseTime(finishTime - startTime);
            stats.addProcess();                      

            // Create a new process with the start time and add to a new queue
            scheduled = new Process();
            scheduled.setBurstTime(p.getBurstTime());
            scheduled.setStartTime(startTime);
            scheduled.setName(p.getName());
            scheduledQueue.add(scheduled);            
        }
        // Add the total quanta to finish jobs
        stats.addQuanta(finishTime);
        printTimeChart(scheduledQueue);
        printRoundAvgStats();
        stats.nextRound();
        
        return scheduledQueue;
    }
}

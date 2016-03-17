//Sam Verma
//Process Scheduler - SJF.java
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class SJF extends ProcessScheduler 
{
    @Override
    public Queue<Process> schedule(PriorityQueue<Process> q) 
    {
        int finishTime = 0;
        int startTime;
        Process p;
        Process scheduled;
        Stats stats = this.getStats();
        Queue<Process> scheduledQueue = new LinkedList<>();
        
        //Queue processes that are waiting to run, order by shortest run time
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(10, 
            new Comparator<Process>() 
            {
                @Override
                public int compare(Process p1, Process p2) {
                    if (p1.getBurstTime() == p2.getBurstTime())
                        return p1.getArrivalTime() < p2.getArrivalTime() ? -1 : 1;
                    else
                        return p1.getBurstTime() < p2.getBurstTime() ? -1 : 1;
                }            
            });
        
        while (!q.isEmpty() || !readyQueue.isEmpty())
        {            
            p = readyQueue.isEmpty() ? q.poll() : readyQueue.poll();
                       
            startTime = Math.max((int) Math.ceil(p.getArrivalTime()), finishTime);
            finishTime = startTime + p.getBurstTime();
            
            //Don't start any processes after 100 time slices
            if (startTime > 100) 
                break;
            //Add processes that have arrived to the ready queue
            while (q.peek() != null && q.peek().getArrivalTime() <= finishTime)
                readyQueue.add(q.poll());
            
            //Record the stats 
            stats.addWaitTime(startTime - p.getArrivalTime());
            stats.addTurnaroundTime(finishTime - p.getArrivalTime());
            stats.addResponseTime(finishTime - startTime);
            stats.addProcess();            

            //Create a new process with the calculated start time and add to a new queue
            scheduled = new Process();
            scheduled.setBurstTime(p.getBurstTime());
            scheduled.setStartTime(startTime);
            scheduled.setName(p.getName());
            scheduledQueue.add(scheduled);            
        }
        //Add the total quanta to finish jobs
        stats.addQuanta(finishTime);
        printTimeChart(scheduledQueue);
        printRoundAvgStats();
        stats.nextRound();
        
        return scheduledQueue;
    }
}

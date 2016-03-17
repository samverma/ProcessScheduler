//Sam Verma
//ProcessScheduler - RR.java
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;


public class RR extends ProcessScheduler 
{    
    @Override
    public Queue<Process> schedule(PriorityQueue<Process> q) 
    {
        int startTime, finishTime = 0;
        Process p, scheduled, remaining;
        ProcessScheduler.Stats stats = this.getStats();
        
        Map<Character, Integer> startTimes = new HashMap<>();        
        Map<Character, Integer> finishTimes = new HashMap<>();
        
        Queue<Process> readyQueue = new LinkedList<>();        
        Queue<Process> waitingQueue = new LinkedList<>();
        Queue<Process> scheduledQueue = new LinkedList<>();     
        
        while (!q.isEmpty() || !readyQueue.isEmpty() || !waitingQueue.isEmpty())
        {
            //Add processes that have arrived to the ready queue
            while (!q.isEmpty() && q.peek().getArrivalTime() <= finishTime)
                readyQueue.add(q.poll());
            
            //Choose process to go next
            if (!readyQueue.isEmpty())
                p = readyQueue.poll();
            else if (!q.isEmpty() && waitingQueue.isEmpty())
                p = q.poll();
            else
                p = waitingQueue.poll(); 

            startTime = Math.max((int) Math.ceil(p.getArrivalTime()), finishTime);
            finishTime = startTime + 1;
            
            //Record stats if unseen process
            if (!startTimes.containsKey(p.getName()))
            {
                if (startTime > 100)
                    break;
                startTimes.put(p.getName(), startTime);
                stats.addWaitTime(startTime - p.getArrivalTime());
                stats.addResponseTime(startTime - p.getArrivalTime() + 1);
            }
            else 
            	//Add the wait time 
                stats.addWaitTime(startTime - finishTimes.get(p.getName()));
            
            //Split p into a second process with n-1 time slices and add to waiting queue
            if (p.getBurstTime() > 1)
            {
                
                remaining = (Process) p.copyProc();
                remaining.setBurstTime(remaining.getBurstTime() - 1);
                waitingQueue.add(remaining);
                finishTimes.put(remaining.getName(), finishTime);
              
            }
            else 
            //This process finished, record turnaround time
            {
                stats.addTurnaroundTime(finishTime - startTimes.get(p.getName()));
                stats.addProcess();
            }            
            //Create a new process with the calculated start time and add to a new queue
            scheduled = new Process();
            scheduled.setBurstTime(1);
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

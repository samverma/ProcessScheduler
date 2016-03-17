//Sam Verma
//ProcessScheduler - SRT.java
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;


 class SRT extends ProcessScheduler 
{    
    @Override
    public Queue<Process> schedule(PriorityQueue<Process> q) 
    {
        int finishTime = 0;
        int startTime;
        Process p;
        Process scheduled;
        Process remaining;
        ProcessScheduler.Stats stats = this.getStats();
        Queue<Process> scheduledQueue = new LinkedList<>();
        
        //Track these to calculate turnaround and wait times
        Map<Character, Integer> startTimes = new HashMap<>();
        Map<Character, Integer> finishTimes = new HashMap<>();
        
        //Queue processes that are ready to run, order by shortest run time
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(10, 
            new Comparator<Process>()
            {
                @Override
                public int compare(Process p1, Process p2) 
                {
                    if (p1.getBurstTime() == p2.getBurstTime())
                        return p1.getArrivalTime() <= p2.getArrivalTime() ? -1 : 1;
                    else
                        return p1.getBurstTime() < p2.getBurstTime() ? -1 : 1;
                }            
            });
        
        //Queue processes that are waiting by priority and remaining time
        PriorityQueue<Process> waitingQueue = new PriorityQueue<>(10, 
            new Comparator<Process>()
            {
                @Override
                public int compare(Process p1, Process p2) 
                {
                    if (p1.getBurstTime() == p2.getBurstTime())
                        return p1.getArrivalTime() <= p2.getArrivalTime() ? -1 : 1;
                    else
                        return p1.getBurstTime() < p2.getBurstTime() ? -1 : 1;
                }            
            });
        
        while (!q.isEmpty() || !readyQueue.isEmpty() || !waitingQueue.isEmpty())
        {
            //Add processes that have arrived to the ready queue
            while (!q.isEmpty() && q.peek().getArrivalTime() <= finishTime)
                readyQueue.add(q.poll());
            
            //Get the process with the shortest remaining time that can start
            //Prioritize ones running
            if (readyQueue.isEmpty())
                p = (waitingQueue.isEmpty()) ? q.poll() : waitingQueue.poll();
            else if (waitingQueue.isEmpty())
                p = readyQueue.poll();
            else
                p = (readyQueue.peek().getBurstTime() < waitingQueue.peek().getBurstTime()) 
                  ? readyQueue.poll()
                  : waitingQueue.poll();
            
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

//Sam Verma
// Process Scheduler-Main.java
import java.util.ArrayList;
import java.util.PriorityQueue;


public class Main
{
    private static final int RUNS = 5;
    private static final int MAX_PROCS_PER_RUN = 10;
    
    public static void main(String[] args) 
    {                
        //Have a scheduler for each of the 4 scheduling algorithms
        ProcessScheduler fcfs = new FCFS();
        ProcessScheduler sjf = new SJF();
        ProcessScheduler rr = new RR();
        ProcessScheduler srt = new SRT();
  
        int algos=4;
        // Hold duplicated process queues for each algorithm to use
        PriorityQueue<Process>[] q = new PriorityQueue[algos + 1];
        q = (PriorityQueue<Process>[]) q;
        
        // Test each algorithm RUNS number of times
        for (int i = 0; i < RUNS; ++i)
        {
            
            System.out.format("Scheduling Process Queue %d:\n", i + 1);
            
            //generate a new process queue for this testing round then duplicate it
            q[0] = Seeder.generateProcesses(MAX_PROCS_PER_RUN);
            for (int j = 1; j < algos + 1; ++j)
                q[j] = copyQueue(q[0]);
            
            // Print the process list by ascending arrival time   
            while (!q[algos].isEmpty())
                System.out.println(q[algos].poll());
                        
            // Run each scheduling algorithm and show the results
          
            System.out.println("\nFirst Come First Served");
            fcfs.schedule(q[0]);
            
            System.out.println("\nShortest Job First");
            sjf.schedule(q[1]);
            
            System.out.println("\nShortest Remaining Time");
            srt.schedule(q[2]);
      
            System.out.println("\nRound Robin");
            rr.schedule(q[3]);            
            
        
        }
        System.out.println();
        System.out.println("Average Statistics by Scheduling Algorithm:");
        System.out.println();

        System.out.println("\nFirst Come First Served");
        fcfs.printAvgStats();

        System.out.println("\nShortest Job First");
        sjf.printAvgStats();

        System.out.println("\nShortest Remaining Time");
        srt.printAvgStats();

        System.out.println("\nRound Robin");
        rr.printAvgStats();            

    }
    
    private static PriorityQueue<Process> copyQueue(PriorityQueue<Process> q) 
    {        
        PriorityQueue<Process> qcopy = new PriorityQueue<>();
        ArrayList<Process> qoriginal = new ArrayList<>();
        while (!q.isEmpty())
        {
            Process p = q.poll();
            qcopy.add((Process) p.copyProc());
            qoriginal.add(p);
        }
        q.addAll(qoriginal);
        return qcopy;
    }
}


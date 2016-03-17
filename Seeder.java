//Sam Verma
//Process Scheduler - Seeder.java
import java.util.PriorityQueue;
import java.util.Random;

//Generates processes to seed the queue with 
public class Seeder
{
    public static PriorityQueue<Process> generateProcesses(int processCount)
    {
        // Use a priority queue to order processes by arrival time
        PriorityQueue<Process> q = new PriorityQueue<>();
        String letters ="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        // get random arrival, expected time, and priority
        Random randArrival = new Random();
        Random randPriority = new Random();        
        Random randExpectedTime = new Random();
        
        double nextArrival = 0.0;

        // Generate new processes and add to the process queue 
        for(int i = 0; i != processCount && nextArrival < 95; ++i)
        {		
            Process p = new Process();
            p.setArrivalTime(nextArrival); 
            p.setBurstTime(randExpectedTime.nextInt(10) + 1);
            p.setPriority(randPriority.nextInt(4) + 1);
            p.setName(letters.charAt(i));
            q.add(p);
            
            nextArrival += randArrival.nextFloat() * 10;
        }
        return q;
    }
}

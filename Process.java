//Sam Verma
//Process Scheduler - Process.java
public class Process implements Comparable<Process> 
{   
   
    public Object copyProc()
    {
        Process copyproc = new Process();
        copyproc.name = this.name;
        copyproc.arrivalTime = this.arrivalTime;
        copyproc.priority = this.priority;
        copyproc.burstTime = this.burstTime;
        copyproc.startTime = this.startTime;
        return copyproc;
    }

    private char name;
    private double arrivalTime;
    private int priority;   
    private int burstTime;    
    private int startTime;     

    public double getArrivalTime() { return arrivalTime; }
    public int getBurstTime() { return burstTime; }
    public int getPriority() { return priority; }
    public int getStartTime() { return startTime; }
    public char getName() { return this.name; }
    
    public void setArrivalTime(double arrivalTime) { this.arrivalTime = arrivalTime; }
    public void setBurstTime(int burstTime) { this.burstTime = burstTime; }
    public void setPriority(int priority) { this.priority = priority; }
    public void setStartTime(int startTime) { this.startTime = startTime; }
    public void setName(char name){ this.name = name; }    

    @Override
	public int compareTo(Process o) {
	      return this.arrivalTime < o.arrivalTime ? -1 : 1;
	}	
    @Override
    public String toString() 
    {
        return String.format(
                "    Process %c [arrivalTime=%f, expectedRunTime=%d, priority=%d]",
                name, arrivalTime, burstTime, priority);
    }
}

#include <climits>
#include <queue>
#include <vector>
#include <algorithm>
#include <iostream>
using namespace std;

struct Process {
    int pid, at, bt, priority;
    int wt = 0, tat = 0, ct = 0, remaining_bt;
};

// Utility function to safely input integer values
int safeInput(string prompt) {
    int val;
    while (true) {
        cout << prompt;
        if (cin >> val && val >= 0) break;
        cout << "Invalid input! Please enter a non-negative integer.\n";
        cin.clear();
        cin.ignore(numeric_limits<streamsize>::max(), '\n');
    }
    return val;
}

// Display process table
void display(vector<Process> &p) {
    double totalWT = 0, totalTAT = 0;
    cout << "\nPID\tAT\tBT\tWT\tTAT\tCT\n";
    for (auto &pr : p) {
        cout << "P" << pr.pid << "\t" << pr.at << "\t" << pr.bt << "\t"
             << pr.wt << "\t" << pr.tat << "\t" << pr.ct << "\n";
        totalWT += pr.wt;
        totalTAT += pr.tat;
    }
    cout << "\nAverage Waiting Time: " << totalWT / p.size();
    cout << "\nAverage Turnaround Time: " << totalTAT / p.size() << "\n";
}

//--------------------------------------------
// 1. FCFS
//--------------------------------------------
void fcfs(vector<Process> p) {
    sort(p.begin(), p.end(), [](auto &a, auto &b) { return a.at < b.at; });
    int time = 0;
    for (auto &pr : p) {
        if (time < pr.at) time = pr.at;
        time += pr.bt;
        pr.ct = time;
        pr.tat = pr.ct - pr.at;
        pr.wt = pr.tat - pr.bt;
    }
    cout << "\n--- FCFS Scheduling ---";
    display(p);
}

//--------------------------------------------
// 2. SJF (Non-preemptive)
//--------------------------------------------
void sjf_nonpreemptive(vector<Process> p) {
    int n = p.size(), completed = 0, time = 0;
    vector<bool> done(n, false);
    while (completed < n) {
        int idx = -1, minBT = INT_MAX;
        for (int i = 0; i < n; i++)
            if (!done[i] && p[i].at <= time && p[i].bt < minBT) {
                minBT = p[i].bt;
                idx = i;
            }

        if (idx == -1) { time++; continue; }
        time += p[idx].bt;
        p[idx].ct = time;
        p[idx].tat = p[idx].ct - p[idx].at;
        p[idx].wt = p[idx].tat - p[idx].bt;
        done[idx] = true;
        completed++;
    }
    cout << "\n--- SJF (Non-Preemptive) Scheduling ---";
    display(p);
}

//--------------------------------------------
// 3. SRTF (Preemptive SJF)
//--------------------------------------------
void srtf(vector<Process> p) {
    int n = p.size(), completed = 0, time = 0;
    for (auto &pr : p) pr.remaining_bt = pr.bt;

    while (completed < n) {
        int idx = -1, minBT = INT_MAX;
        for (int i = 0; i < n; i++)
            if (p[i].at <= time && p[i].remaining_bt > 0 && p[i].remaining_bt < minBT) {
                minBT = p[i].remaining_bt;
                idx = i;
            }

        if (idx == -1) { time++; continue; }
        p[idx].remaining_bt--;
        time++;

        if (p[idx].remaining_bt == 0) {
            completed++;
            p[idx].ct = time;
            p[idx].tat = p[idx].ct - p[idx].at;
            p[idx].wt = p[idx].tat - p[idx].bt;
        }
    }
    cout << "\n--- SRTF (Preemptive SJF) Scheduling ---";
    display(p);
}

//--------------------------------------------
// 4. Priority Scheduling (Preemptive & Non)
//--------------------------------------------
void priorityScheduling(vector<Process> p, bool preemptive) {
    int n = p.size(), completed = 0, time = 0;
    for (auto &pr : p) pr.remaining_bt = pr.bt;

    vector<bool> done(n, false);

    while (completed < n) {
        int idx = -1, highestPriority = INT_MAX;
        for (int i = 0; i < n; i++)
            if (p[i].at <= time && p[i].remaining_bt > 0 && p[i].priority < highestPriority) {
                highestPriority = p[i].priority;
                idx = i;
            }

        if (idx == -1) { time++; continue; }

        if (preemptive) {
            p[idx].remaining_bt--;
            time++;
            if (p[idx].remaining_bt == 0) {
                p[idx].ct = time;
                p[idx].tat = p[idx].ct - p[idx].at;
                p[idx].wt = p[idx].tat - p[idx].bt;
                completed++;
            }
        } else {
            time += p[idx].bt;
            p[idx].ct = time;
            p[idx].tat = p[idx].ct - p[idx].at;
            p[idx].wt = p[idx].tat - p[idx].bt;
            p[idx].remaining_bt = 0;
            completed++;
        }
    }
    cout << (preemptive ? "\n--- Priority (Preemptive)" : "\n--- Priority (Non-Preemptive)");
    display(p);
}

//--------------------------------------------
// 5. Round Robin
//--------------------------------------------
void roundRobin(vector<Process> p) {
    int quantum = safeInput("Enter Time Quantum: ");
    int n = p.size();
    queue<int> q;
    int time = 0, completed = 0;
    for (auto &pr : p) pr.remaining_bt = pr.bt;
    vector<bool> inQueue(n, false);

    while (completed < n) {
        for (int i = 0; i < n; i++)
            if (!inQueue[i] && p[i].at <= time && p[i].remaining_bt > 0) {
                q.push(i);
                inQueue[i] = true;
            }

        if (q.empty()) { time++; continue; }

        int idx = q.front(); q.pop();
        inQueue[idx] = false;

        int runTime = min(quantum, p[idx].remaining_bt);
        p[idx].remaining_bt -= runTime;
        time += runTime;

        for (int i = 0; i < n; i++)
            if (!inQueue[i] && p[i].at <= time && p[i].remaining_bt > 0) {
                q.push(i);
                inQueue[i] = true;
            }

        if (p[idx].remaining_bt > 0) {
            q.push(idx);
            inQueue[idx] = true;
        } else {
            completed++;
            p[idx].ct = time;
            p[idx].tat = p[idx].ct - p[idx].at;
            p[idx].wt = p[idx].tat - p[idx].bt;
        }
    }

    cout << "\n--- Round Robin Scheduling ---";
    display(p);
}

//--------------------------------------------
// Main Menu
//--------------------------------------------
int main() {
    cout << "==== CPU Scheduling Simulator ====\n";
    while (true) {
        cout << "\nMenu:\n";
        cout << "1. FCFS\n";
        cout << "2. SJF (Non-Preemptive)\n";
        cout << "3. SRTF (Preemptive SJF)\n";
        cout << "4. Priority (Non-Preemptive)\n";
        cout << "5. Priority (Preemptive)\n";
        cout << "6. Round Robin\n";
        cout << "7. Exit\n";

        int choice = safeInput("Enter your choice: ");
        if (choice == 7) break;

        int n = safeInput("Enter number of processes: ");
        vector<Process> p(n);
        for (int i = 0; i < n; i++) {
            p[i].pid = i + 1;
            p[i].at = safeInput("Arrival time for P" + to_string(i + 1) + ": ");
            p[i].bt = safeInput("Burst time for P" + to_string(i + 1) + ": ");
            if (choice == 4 || choice == 5)
                p[i].priority = safeInput("Priority for P" + to_string(i + 1) + " (lower = higher): ");
        }

        switch (choice) {
            case 1: fcfs(p); break;
            case 2: sjf_nonpreemptive(p); break;
            case 3: srtf(p); break;
            case 4: priorityScheduling(p, false); break;
            case 5: priorityScheduling(p, true); break;
            case 6: roundRobin(p); break;
            default: cout << "Invalid choice.\n";
        }
    }
    cout << "\nExiting CPU Scheduler. Goodbye!\n";
    return 0;
}

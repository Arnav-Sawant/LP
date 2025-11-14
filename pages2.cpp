#include <bits/stdc++.h>
using namespace std;

// Function to safely input integers
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

//-----------------------------------------
// FIFO Page Replacement
//-----------------------------------------
int FIFO(vector<int> pages, int frames) {
    queue<int> q;
    unordered_set<int> s;
    int faults = 0;

    for (int page : pages) {
        if (s.find(page) == s.end()) {
            if (s.size() == frames) {
                int victim = q.front();
                q.pop();
                s.erase(victim);
            }
            s.insert(page);
            q.push(page);
            faults++;
        }
    }
    return faults;
}

//-----------------------------------------
// LRU Page Replacement
//-----------------------------------------
int LRU(vector<int> pages, int frames) {
    list<int> cache;
    unordered_map<int, list<int>::iterator> pos;
    int faults = 0;

    for (int page : pages) {
        if (pos.find(page) == pos.end()) {
            if ((int)cache.size() == frames) {
                int victim = cache.back();
                cache.pop_back();
                pos.erase(victim);
            }
            faults++;
        } else {
            cache.erase(pos[page]);
        }
        cache.push_front(page);
        pos[page] = cache.begin();
    }
    return faults;
}

//-----------------------------------------
// OPT (Optimal) Page Replacement
//-----------------------------------------
int OPT(vector<int> pages, int frames) {
    vector<int> memory;
    int faults = 0;

    for (size_t i = 0; i < pages.size(); i++) {
        int page = pages[i];
        // If not in memory
        if (find(memory.begin(), memory.end(), page) == memory.end()) {
            if ((int)memory.size() < frames) {
                memory.push_back(page);
            } else {
                // Find victim page (farthest future use)
                int farthest = -1, victimIndex = -1;
                for (int j = 0; j < (int)memory.size(); j++) {
                    int nextUse = INT_MAX;
                    for (size_t k = i + 1; k < pages.size(); k++) {
                        if (memory[j] == pages[k]) {
                            nextUse = k;
                            break;
                        }
                    }
                    if (nextUse > farthest) {
                        farthest = nextUse;
                        victimIndex = j;
                    }
                }
                memory[victimIndex] = page;
            }
            faults++;
        }
    }
    return faults;
}

//-----------------------------------------
// Utility: Print results
//-----------------------------------------
void showResults(string name, int faults, int total, int frames) {
    double hit = total - faults;
    double hitRatio = (hit / total) * 100.0;
    cout << "\nAlgorithm: " << name;
    cout << "\nPage Faults: " << faults;
    cout << "\nPage Hits: " << hit;
    cout << "\nHit Ratio: " << fixed << setprecision(2) << hitRatio << "%\n";
}

//-----------------------------------------
// Main Program
//-----------------------------------------
int main() {
    cout << "===== Page Replacement Algorithms =====\n";

    while (true) {
        cout << "\nMenu:\n";
        cout << "1. FIFO\n";
        cout << "2. LRU\n";
        cout << "3. OPT (Optimal)\n";
        cout << "4. Compare All\n";
        cout << "5. Exit\n";

        int choice = safeInput("Enter your choice: ");
        if (choice == 5) break;

        int n = safeInput("Enter number of pages in reference string: ");
        vector<int> pages(n);

        cout << "Enter reference string (" << n << " pages): ";
        for (int i = 0; i < n; i++) {
            while (!(cin >> pages[i])) {
                cout << "Invalid input! Enter integer page number: ";
                cin.clear();
                cin.ignore(numeric_limits<streamsize>::max(), '\n');
            }
        }

        int frames = safeInput("Enter number of frames: ");

        int f1 = 0, f2 = 0, f3 = 0;
        switch (choice) {
            case 1:
                f1 = FIFO(pages, frames);
                showResults("FIFO", f1, n, frames);
                break;
            case 2:
                f2 = LRU(pages, frames);
                showResults("LRU", f2, n, frames);
                break;
            case 3:
                f3 = OPT(pages, frames);
                showResults("OPT", f3, n, frames);
                break;
            case 4:
                f1 = FIFO(pages, frames);
                f2 = LRU(pages, frames);
                f3 = OPT(pages, frames);
                showResults("FIFO", f1, n, frames);
                showResults("LRU", f2, n, frames);
                showResults("OPT", f3, n, frames);
                break;
            default:
                cout << "Invalid choice. Try again.\n";
        }
    }

    cout << "\nExiting Page Replacement Simulator. Goodbye!\n";
    return 0;
}

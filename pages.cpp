#include <bits/stdc++.h>
using namespace std;

void printFrames(const vector<int>& frames) {
    for (int f : frames) {
        if (f == -1) cout << "- ";
        else cout << f << " ";
    }
    cout << endl;
}

void fifo(vector<int> pages, int framesCount) {
    vector<int> frames(framesCount, -1);
    int pageFaults = 0, index = 0;

    cout << "\n--- FIFO Page Replacement ---\n";
    for (int p : pages) {
        if (find(frames.begin(), frames.end(), p) == frames.end()) {
            frames[index] = p;
            index = (index + 1) % framesCount;
            pageFaults++;
        }
        printFrames(frames);
    }
    cout << "Total Page Faults (FIFO): " << pageFaults << endl;
}

// void lru(vector<int> pages, int framesCount) {
//     vector<int> frames;
//     int pageFaults = 0;

//     cout << "\n--- LRU Page Replacement ---\n";
//     for (int i = 0; i < pages.size(); i++) {
//         int p = pages[i];
//         auto it = find(frames.begin(), frames.end(), p);

//         if (it == frames.end()) {
//             if (frames.size() < framesCount) {
//                 frames.push_back(p);
//             } else {
//                 int lruIndex = -1, farthest = i;
//                 for (int j = 0; j < frames.size(); j++) {
//                     int k;
//                     for (k = i - 1; k >= 0; k--) {
//                         if (pages[k] == frames[j]) break;
//                     }
//                     if (k < farthest) {
//                         farthest = k;
//                         lruIndex = j;
//                     }
//                 }
//                 frames[lruIndex] = p;
//             }
//             pageFaults++;
//         } else {
//             frames.erase(it);
//             frames.push_back(p);
//         }

//         printFrames(frames);
//     }
//     cout << "Total Page Faults (LRU): " << pageFaults << endl;
// }



void LRU(vector<int> pages, int capacity) {
    vector<int> memory;  
    int pageFaults = 0;

    for (int page : pages) {
        auto it = find(memory.begin(), memory.end(), page);
        if (it == memory.end()) {
            pageFaults++;
            if (memory.size() == capacity)
                memory.erase(memory.begin());
        } else {
            memory.erase(it);
        }

        memory.push_back(page);

        cout << "Page: " << page << " -> [ ";
        for (int p : memory) cout << p << " ";
        cout << "]" << endl;
    }

    cout << "\nTotal Page Faults = " << pageFaults << endl;
}


void optimal(vector<int> pages, int framesCount) {
    vector<int> frames;
    int pageFaults = 0;

    cout << "\n--- Optimal Page Replacement ---\n";
    for (int i = 0; i < pages.size(); i++) {
        int p = pages[i];
        auto it = find(frames.begin(), frames.end(), p);

        if (it == frames.end()) {
            if (frames.size() < framesCount) {
                frames.push_back(p);
            } else {
                int farthest = i, replaceIndex = -1;
                for (int j = 0; j < frames.size(); j++) {
                    int k;
                    for (k = i + 1; k < pages.size(); k++) {
                        if (pages[k] == frames[j]) break;
                    }
                    if (k > farthest) {
                        farthest = k;
                        replaceIndex = j;
                    }
                }
                if (replaceIndex == -1) replaceIndex = 0;
                frames[replaceIndex] = p;
            }
            pageFaults++;
        }
        printFrames(frames);
    }
    cout << "Total Page Faults (Optimal): " << pageFaults << endl;
}

int main() {
    int n, framesCount, choice;
    cout << "Enter number of pages: ";
    cin >> n;

    vector<int> pages(n);
    cout << "Enter page reference string: ";
    for (int i = 0; i < n; i++) cin >> pages[i];

    cout << "Enter number of frames: ";
    cin >> framesCount;

    do {
        cout << "\nChoose Algorithm:\n";
        cout << "1. FIFO\n2. LRU\n3. Optimal\n4. Exit\nChoice: ";
        cin >> choice;

        switch (choice) {
            case 1: fifo(pages, framesCount); break;
            case 2: lru(pages, framesCount); break;
            case 3: optimal(pages, framesCount); break;
            case 4: cout << "Exiting...\n"; break;
            default: cout << "Invalid choice!\n";
        }
    } while (choice != 4);

    return 0;
}


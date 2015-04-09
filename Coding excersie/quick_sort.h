#include<iostream>
using namespace std;

int partition(int a[],int p, int r){
	int x = a[r];
	int i = p - 1;
	for (int j = p; j <= (r - 1); j++){
		if (a[j] <= x){
			i++;
			int temp=a[j];
			a[j] = a[i];
			a[i] = temp;		
		}
	}
	int temp2 = a[i+1];
	a[i + 1] = a[r];
	a[r] = temp2;
	return i + 1;//i+1是pivot的index
}


void quicksort(int a[],int p, int r){
	if (p < r){
		int q = partition(a, p, r);
		quicksort(a, p, q - 1);
		quicksort(a, q + 1, r);
	}
	
}

void main(){
	int a[] = { 3, 5, 6, 8, 0, 4 };
	int size = sizeof(a) / sizeof(a[0]);
	quicksort(a, 0, size-1);
	cout<<"The index of pivot is:"<<partition(a, 0, 5)<<endl;
	cout << "The sorted array is:   ";
	for (int i = 0; i < size; i++){
		cout << a[i] << " ";
	}
	
	system("pause");
}
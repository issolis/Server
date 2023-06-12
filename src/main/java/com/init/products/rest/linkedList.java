package com.init.products.rest;

public class linkedList {
	node head= null;
	int length=0; 
	
	
	void insert(String data) {
		node newNode = new node();
		newNode.data=data; 
		if(head==null) {
			head=newNode; 
		}
		else {
			node aux=head;
			while(aux.next!=null)
				aux=aux.next; 
			aux.next=newNode; 
		}
		length++; 
	}
}

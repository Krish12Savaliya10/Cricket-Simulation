package DataStructure;

import Simulation.Team;

public class StackOfTeam {
    private static class Node {
        Team data;
        Node next;

        Node(Team data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node top;


    public StackOfTeam() {
        top = null;
    }


    public void push(Team team) {
        Node newNode = new Node(team);
        newNode.next = top;
        top = newNode;
    }


    public Team pop() {
        if (isEmpty()) {
            System.out.println("Team stack is empty!");
            return null;
        }
        Team popped = top.data;
        top = top.next;
        return popped;
    }


    public Team peek() {
        if (isEmpty()) {
            return null;
        }
        return top.data;
    }


    public boolean isEmpty() {
        return top == null;
    }


    public void printStack() {
        Node current = top;
        System.out.println("Team Stack (Top to Bottom):");
        while (current != null) {
            System.out.println(current.data.getTeamName()); // assuming getTeamName() exists
            current = current.next;
        }
    }
}

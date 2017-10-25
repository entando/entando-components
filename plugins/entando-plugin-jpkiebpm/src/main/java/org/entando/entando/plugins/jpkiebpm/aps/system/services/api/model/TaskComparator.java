/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.plugins.jpkiebpm.aps.system.services.api.model;

import java.util.Comparator;

/**
 *
 * @author paco
 */
public class TaskComparator implements Comparator<JAXBTask> {

    public static class Activated implements Comparator<JAXBTask> {

        @Override
        public int compare(JAXBTask o1, JAXBTask o2) {
            return (int) (o1.getActivated() - o2.getActivated());
        }

    }

    public static class Created implements Comparator<JAXBTask> {

        @Override
        public int compare(JAXBTask o1, JAXBTask o2) {
            return (int) (o1.getCreated() - o2.getCreated());
        }

    }

    public static class Name implements Comparator<JAXBTask> {

        @Override
        public int compare(JAXBTask o1, JAXBTask o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }

    }

    public static class Priority implements Comparator<JAXBTask> {

        @Override
        public int compare(JAXBTask o1, JAXBTask o2) {
            return (int) (o1.getPriority() - o2.getPriority());
        }

    }

    public static class ProcessDefinitionId implements Comparator<JAXBTask> {

        @Override
        public int compare(JAXBTask o1, JAXBTask o2) {
            return o1.getProcessDefinitionId().compareToIgnoreCase(o2.getProcessDefinitionId());
        }

    }

    public static class ProcessInstanceId implements Comparator<JAXBTask> {

        @Override
        public int compare(JAXBTask o1, JAXBTask o2) {
            return (int) (o1.getProcessInstanceId() - o2.getProcessInstanceId());
        }

    }

    public static class Skipable implements Comparator<JAXBTask> {

        @Override
        public int compare(JAXBTask o1, JAXBTask o2) {
            return o1.getSkipable().compareTo(o2.getSkipable());
        }

    }

    public static class Status implements Comparator<JAXBTask> {

        @Override
        public int compare(JAXBTask o1, JAXBTask o2) {
            return o1.getStatus().compareToIgnoreCase(o2.getStatus());
        }

    }

    @Override
    public int compare(JAXBTask o1, JAXBTask o2) {
        return (int) (o1.getId() - o2.getId());
    }

}

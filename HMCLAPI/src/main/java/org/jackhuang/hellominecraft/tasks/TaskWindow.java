/*
 * Copyright 2013 huangyuhui <huanghongxun2008@126.com>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.
 */
package org.jackhuang.hellominecraft.tasks;

import java.awt.EventQueue;
import java.util.ArrayList;
import org.jackhuang.hellominecraft.C;
import org.jackhuang.hellominecraft.utils.functions.NonConsumer;
import org.jackhuang.hellominecraft.HMCLog;
import org.jackhuang.hellominecraft.utils.system.MessageBox;
import org.jackhuang.hellominecraft.utils.StrUtils;
import org.jackhuang.hellominecraft.utils.SwingUtils;

/**
 *
 * @author hyh
 */
public class TaskWindow extends javax.swing.JDialog
        implements ProgressProviderListener, NonConsumer, DoingDoneListener<Task> {

    private static final TaskWindow instance = new TaskWindow();

    public static TaskWindow getInstance() {
        instance.clean();
        return instance;
    }

    boolean suc = false;

    private TaskList taskList;
    private final ArrayList<String> failReasons = new ArrayList();

    /**
     * Creates new form DownloadWindow
     */
    private TaskWindow() {
        initComponents();

        setLocationRelativeTo(null);

        setModal(true);
    }

    public TaskWindow addTask(Task task) {
        taskList.addTask(task);
        return this;
    }

    public void clean() {
        taskList = null;
        taskList = new TaskList();
        taskList.addTaskListener(this);
        taskList.addAllDoneListener(this);
    }

    public boolean start() {
        if (taskList.isAlive()) return false;
        pgsSingle.setValue(0);
        pgsTotal.setValue(0);
        suc = false;
        SwingUtils.clear(lstDownload);
        failReasons.clear();
        try {
            taskList.start();
        } catch (Exception e) {
            HMCLog.warn("Failed to start thread, maybe there're already a taskwindow here.", e);
            MessageBox.Show(C.i18n("taskwindow.no_more_instance"));
            return false;
        }
        this.setVisible(true);
        return this.areTasksFinished();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnCancel = new javax.swing.JButton();
        pgsSingle = new javax.swing.JProgressBar();
        lblSingleProgress = new javax.swing.JLabel();
        lblTotalProgress = new javax.swing.JLabel();
        pgsTotal = new javax.swing.JProgressBar();
        srlDownload = new javax.swing.JScrollPane();
        lstDownload = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/jackhuang/hellominecraft/launcher/I18N"); // NOI18N
        setTitle(bundle.getString("taskwindow.title")); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        btnCancel.setText(bundle.getString("taskwindow.cancel")); // NOI18N
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        pgsSingle.setStringPainted(true);

        lblSingleProgress.setText(bundle.getString("taskwindow.single_progress")); // NOI18N

        lblTotalProgress.setText(bundle.getString("taskwindow.total_progress")); // NOI18N

        pgsTotal.setStringPainted(true);

        srlDownload.setViewportView(lstDownload);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblSingleProgress)
                        .addGap(349, 349, 349))
                    .addComponent(pgsSingle, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblTotalProgress)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(pgsTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancel)
                .addContainerGap())
            .addComponent(srlDownload)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(srlDownload, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCancel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblSingleProgress)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pgsSingle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotalProgress)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pgsTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        if(MessageBox.Show(C.i18n("operation.confirm_stop"), MessageBox.YES_OPTION) == MessageBox.YES_OPTION)
            this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        if (!this.failReasons.isEmpty()) {
            MessageBox.Show(StrUtils.parseParams("", failReasons.toArray(), "\n"), C.i18n("message.error"), MessageBox.ERROR_MESSAGE);
            failReasons.clear();
        }

        if (!suc) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    taskList.abort();
                }
            });
            HMCLog.log("Tasks have been canceled by user.");
        }
    }//GEN-LAST:event_formWindowClosed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JLabel lblSingleProgress;
    private javax.swing.JLabel lblTotalProgress;
    private javax.swing.JList lstDownload;
    private javax.swing.JProgressBar pgsSingle;
    private javax.swing.JProgressBar pgsTotal;
    private javax.swing.JScrollPane srlDownload;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setProgress(int progress, int max) {
        pgsSingle.setMaximum(max);
        pgsSingle.setValue(progress);
    }

    @Override
    public void onDone() {
        suc = true;
        this.dispose();
        HMCLog.log("Tasks are finished.");
    }

    @Override
    public void onDoing(Task task) {
        if (!task.isParallelExecuting())
            task.setProgressProviderListener(this);

        SwingUtils.appendLast(lstDownload, task.getInfo());
        SwingUtils.moveEnd(srlDownload);
    }

    public boolean areTasksFinished() {
        return suc;
    }

    @Override
    public void onDone(Task task) {
        pgsTotal.setMaximum(taskList.taskCount());
        pgsTotal.setValue(pgsTotal.getValue() + 1);
    }

    @Override
    public void onFailed(Task task) {
        failReasons.add(task.getInfo() + ": " + (task.getFailReason() == null ? "No exception" : task.getFailReason().getLocalizedMessage()));
        pgsTotal.setMaximum(taskList.taskCount());
        pgsTotal.setValue(pgsTotal.getValue() + 1);
        SwingUtils.replaceLast(lstDownload, task.getFailReason());
        SwingUtils.moveEnd(srlDownload);
    }

    @Override
    public void onProgressProviderDone() {

    }

    @Override
    public void setStatus(String sta) {
        SwingUtils.replaceLast(lstDownload, sta);
    }
}

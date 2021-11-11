//package cn.weihu.kol.job;
//
//import cn.hutool.core.date.DatePattern;
//import cn.hutool.core.date.DateUtil;
//import cn.weihu.kol.biz.ControlRuleBiz;
//import cn.weihu.kol.biz.SeaTableBiz;
//import cn.weihu.kol.biz.TaskBiz;
//import cn.weihu.kol.biz.bo.Dispatch;
//import cn.weihu.kol.container.TaskContainer;
//import cn.weihu.kol.db.po.ControlRule;
//import cn.weihu.kol.db.po.Task;
//import cn.weihu.kol.enums.TaskStatusEnums;
//import cn.weihu.kol.redis.RedisUtils;
//import cn.weihu.kol.runner.StartupRunner;
//import cn.weihu.kol.util.GsonUtils;
//import com.google.gson.reflect.TypeToken;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.util.CollectionUtils;
//
//import java.lang.reflect.Type;
//import java.util.List;
//
//@Slf4j
//@Component
//public class MonitorJob {
//
//    @Autowired
//    private RedisUtils     redisUtils;
//    @Autowired
//    private TaskBiz        taskBiz;
//    @Autowired
//    private ControlRuleBiz controlRuleBiz;
//    @Autowired
//    private SeaTableBiz    seaTableBiz;
//    @Autowired
//    private TaskContainer  taskContainer;
//
//    @Scheduled(fixedRate = 30000)
//    public void checkRunTask() {
//        if(redisUtils.getLocalObcStatus()) {
//            if(!redisUtils.isManageGet(StartupRunner.stationNO)) {
//                log.info("非主调度服务,不进行任务调度时间控制");
//                return;
//            }
//            long startTime = System.currentTimeMillis();
//            log.debug("自动任务开始时间[" + startTime + "]");
//
//            List<Task> unfinishedList = taskBiz.getUnfinishedList(TaskStatusEnums.Running.getCode());
//            if(!CollectionUtils.isEmpty(unfinishedList)) {
//                String      ruleId;
//                ControlRule controlRule;
//                String      callStartDate;
//                String      callEndDate;
//                for(Task task : unfinishedList) {
//                    ruleId = task.getRuleId();
//                    controlRule = controlRuleBiz.getById(ruleId);
//                    callStartDate = DateUtil.format(controlRule.getCallStartTime(), DatePattern.NORM_DATE_PATTERN);
//                    callEndDate = DateUtil.format(controlRule.getCallEndTime(), DatePattern.NORM_DATE_PATTERN);
//                    if(DateUtil.date().isAfterOrEquals(controlRule.getCallStartTime())) {
//                        // 当前时间大于等于调度的呼叫开始时间
//                        if(DateUtil.date().isBefore(controlRule.getCallEndTime())) {
//                            // 且小于呼叫结束时间
//                            Type type = new TypeToken<List<Dispatch>>() {
//                            }.getType();
//                            List<Dispatch> dispatches = GsonUtils.gson.fromJson(controlRule.getCallTimeRule(), type);
//                            // 判断执行中的任务是否在 可外呼时间段内, 是不处理/否则暂停
//                            if(!taskContainer.checkDispatch(callStartDate, callEndDate, dispatches)) {
//                                execTaskStatus(task.getId(), TaskStatusEnums.Pause_Auto.getCode());
//                                log.debug(">>> 自动暂停任务:{}", task.getName());
//                            }
//                        } else {
//                            // 大于等于呼叫结束时间, 任务触发关闭
//                            execTaskStatus(task.getId(), TaskStatusEnums.Close.getCode());
//                            log.debug(">>> 自动关闭任务:{}", task.getName());
//                        }
//                    } else {
//                        // 非法执行的任务, 任务触发暂停
//                        log.error(">>> 自动检查任务调度线程发现非法执行的任务,taskId:{}", task.getId());
//                        execTaskStatus(task.getId(), TaskStatusEnums.Pause_Auto.getCode());
//                    }
//                }
//            }
//            long endTime = System.currentTimeMillis();
//            log.debug("自动任务结束时间[" + endTime + "]耗费时长" + (endTime - startTime) + "ms");
//        }
//    }
//
//    @Scheduled(fixedRate = 30000)
//    public void checkPauseAutoTask() {
//        if(redisUtils.getLocalObcStatus()) {
//            if(!redisUtils.isManageGet(StartupRunner.stationNO)) {
//                log.info("非主调度服务,不进行任务调度时间控制");
//                return;
//            }
//            long startTime = System.currentTimeMillis();
//            log.debug("自动任务开始时间[" + startTime + "]");
//
//            List<Task> unfinishedList = taskBiz.getUnfinishedList(TaskStatusEnums.Pause_Auto.getCode());
//            if(!CollectionUtils.isEmpty(unfinishedList)) {
//                String      ruleId;
//                ControlRule controlRule;
//                String      callStartDate;
//                String      callEndDate;
//                for(Task task : unfinishedList) {
//                    ruleId = task.getRuleId();
//                    controlRule = controlRuleBiz.getById(ruleId);
//                    callStartDate = DateUtil.format(controlRule.getCallStartTime(), DatePattern.NORM_DATE_PATTERN);
//                    callEndDate = DateUtil.format(controlRule.getCallEndTime(), DatePattern.NORM_DATE_PATTERN);
//                    if(DateUtil.date().isAfterOrEquals(controlRule.getCallStartTime())) {
//                        // 当前时间大于等于调度的呼叫开始时间
//                        if(DateUtil.date().isBefore(controlRule.getCallEndTime())) {
//                            // 且小于呼叫结束时间
//                            Type type = new TypeToken<List<Dispatch>>() {
//                            }.getType();
//                            List<Dispatch> dispatches = GsonUtils.gson.fromJson(controlRule.getCallTimeRule(), type);
//                            // 判断暂停中的任务是否在 可外呼时间段内, 是则启动/否不处理
//                            if(taskContainer.checkDispatch(callStartDate, callEndDate, dispatches)) {
//                                execTaskStatus(task.getId(), TaskStatusEnums.Running.getCode());
//                                log.debug(">>> 自动启动任务:{}", task.getName());
//                            }
//                        } else {
//                            // 大于等于呼叫结束时间, 任务触发关闭
//                            execTaskStatus(task.getId(), TaskStatusEnums.Close.getCode());
//                            log.debug(">>> 自动关闭任务:{}", task.getName());
//                        }
//                    }
//                }
//            }
//            long endTime = System.currentTimeMillis();
//            log.debug("自动任务结束时间[" + endTime + "]耗费时长" + (endTime - startTime) + "ms");
//        }
//    }
//
//    @Scheduled(fixedRate = 30000)
//    public void checkPauseManualTask() {
//        if(redisUtils.getLocalObcStatus()) {
//            if(!redisUtils.isManageGet(StartupRunner.stationNO)) {
//                log.info("非主调度服务,不进行任务调度时间控制");
//                return;
//            }
//            long startTime = System.currentTimeMillis();
//            log.debug("自动任务开始时间[" + startTime + "]");
//
//            List<Task> unfinishedList = taskBiz.getUnfinishedList(TaskStatusEnums.Pause_Manual.getCode());
//            if(!CollectionUtils.isEmpty(unfinishedList)) {
//                String      ruleId;
//                ControlRule controlRule;
//                for(Task task : unfinishedList) {
//                    ruleId = task.getRuleId();
//                    controlRule = controlRuleBiz.getById(ruleId);
//                    if(DateUtil.date().isAfterOrEquals(controlRule.getCallEndTime())) {
//                        // 大于等于呼叫结束时间, 任务触发关闭
//                        execTaskStatus(task.getId(), TaskStatusEnums.Close.getCode());
//                        log.debug(">>> 自动关闭任务:{}", task.getName());
//                    }
//                }
//            }
//            long endTime = System.currentTimeMillis();
//            log.debug("自动任务结束时间[" + endTime + "]耗费时长" + (endTime - startTime) + "ms");
//        }
//    }
//
//    private void execTaskStatus(String taskId, Integer status) {
//        try {
//            taskBiz.status(taskId, status);
//        } catch(Exception e) {
//            log.error(">>> 自动操作任务状态异常,taskId:{},e:{}", taskId, e.getMessage());
//        }
//    }
//
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void pushSeaTable() {
//        if(redisUtils.getLocalObcStatus()) {
//            if(!redisUtils.isManageGet(StartupRunner.stationNO)) {
//                log.info("非主调度服务,不进行海表数据推送");
//                return;
//            }
//            seaTableBiz.statistic();
//            seaTableBiz.statisticMoblieState();
//            log.info(">>> 海表数据推送完成");
//        }
//    }
//}

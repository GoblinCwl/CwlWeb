package com.goblincwl.cwlweb.manager.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.goblincwl.cwlweb.common.entity.Result;
import com.goblincwl.cwlweb.common.web.controller.BaseController;
import com.goblincwl.cwlweb.index.entity.ChatMessage;
import com.goblincwl.cwlweb.index.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * 聊天信息 Controller
 *
 * @author ☪wl
 * @date 2021-05-06 22:36
 */
@RestController
@RequestMapping(ManagerController.MODULE_PREFIX + "/chatMessage")
@RequiredArgsConstructor
public class ChatMessageController extends BaseController<ChatMessage> {

    private final ChatMessageService chatMessageService;

    /**
     * 分页主查询
     *
     * @param chatMessage 查询参数
     * @return 结果集
     * @date 2021-05-06 22:43:02
     * @author ☪wl
     */
    @GetMapping("/list")
    private Result<Page<ChatMessage>> list(ChatMessage chatMessage) {
        return new Result<Page<ChatMessage>>().success(
                this.chatMessageService.page(
                        createPage(),
                        createQueryWrapper(chatMessage)),
                "成功");
    }

    /**
     * 删除
     *
     * @param ids 主键逗号分隔
     * @return 反馈
     * @date 2021-05-06 22:54:46
     * @author ☪wl
     */
    @DeleteMapping("/remove")
    private Result<Object> remove(String ids) {
        if (StringUtils.isNotEmpty(ids)) {
            this.chatMessageService.removeByIds(Arrays.asList(ids.split(",")));
        }
        return Result.genSuccess("删除成功");
    }
}

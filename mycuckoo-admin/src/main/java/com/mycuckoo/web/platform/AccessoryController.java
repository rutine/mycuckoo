package com.mycuckoo.web.platform;

import com.mycuckoo.util.CommonUtils;
import com.mycuckoo.service.platform.AccessoryService;
import com.mycuckoo.core.AjaxResponse;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * 功能说明: 附件Controller
 *
 * @author rutine
 * @version 3.0.0
 * @time Oct 16, 2014 4:47:41 PM
 */
@RestController
@RequestMapping("/platform/accessory/mgr")
public class AccessoryController {
    private static Logger logger = LoggerFactory.getLogger(AccessoryController.class);

    @Autowired
    private AccessoryService accessoryService;


    /**
     * 功能说明 : 根据附件ID删除附件
     *
     * @param filenameOrId
     * @return
     * @author rutine
     * @time Jul 1, 2013 8:58:27 PM
     */
    @DeleteMapping("/{filenameOrId}")
    public AjaxResponse<String> delete(@PathVariable String filenameOrId) {
        long id = NumberUtils.toLong(filenameOrId, -1L);
        if (id == -1L) {
            CommonUtils.deleteFile("", filenameOrId);
        } else {
            accessoryService.deleteByIds(Arrays.asList(id));
        }

        return AjaxResponse.create("附件删除成功");
    }
}

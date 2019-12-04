package io.github.sandy.controllers;


import com.pusher.rest.Pusher;
import io.github.sandy.massages.OneToOneMassages;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;


@RestController
public class OneToOneMassegesController {
    @RequestMapping(value = "/api/send", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void addMasseges(@RequestBody OneToOneMassages massages) {
        Pusher pusher = new Pusher("899299", "b62a6641110833445445", "83aa5943d5de895e8ada");
        pusher.setCluster("ap1");
        pusher.setEncrypted(true);
        pusher.trigger("my-channel", "my-event", Collections.singletonMap("message", massages));
    }
}

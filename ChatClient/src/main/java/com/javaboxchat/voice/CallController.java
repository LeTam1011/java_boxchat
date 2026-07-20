package com.javaboxchat.voice;

import com.javaboxchat.ui.CallFrame;

public class CallController {

    private CallFrame frame;

    private String currentUser;

    private String partner;

    private boolean muted = false;

    public CallFrame getFrame() {
        return frame;
    }

    public CallController(
            String currentUser,
            String partner
    ) {

        this.currentUser = currentUser;
        this.partner = partner;

        frame = new CallFrame(partner);

        frame.setStatus(
                "Đã kết nối"
        );

        initEvents();
    }

    private void initEvents() {

        frame.getBtnMute().addActionListener(e -> {

            muted = !muted;

            if (muted) {

                frame.getBtnMute()
                        .setText(
                                "🎤 Bật Mic"
                        );

            } else {

                frame.getBtnMute()
                        .setText(
                                "🎤 Tắt Mic"
                        );
            }

        });
        frame.getBtnEnd().addActionListener(e -> {

            frame.dispose();

        });

    }

}
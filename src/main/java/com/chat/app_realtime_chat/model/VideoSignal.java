package com.chat.app_realtime_chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoSignal {
    private String type;   // E.g., "CALL", "OFFER", "ANSWER", "ICE", "REJECT", "HANGUP"
    private String sender; // Who is initiating the signal
    private String target; // Who is supposed to receive this signal
    private String data;   // The actual WebRTC network data (JSON stringified)
}
package open.dolphin.client;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import open.dolphin.infomodel.SimpleDate;

/**
 *
 * @author Kazushi Minagawa.
 */
public class PatientVisitInspector {
    
    private CalendarCardPanel calendarCardPanel;

    private String pvtEvent; // PVT
    
    private final ChartImpl context;

    /**
     * PatientVisitInspector を生成する。
     * @param context
     */
    public PatientVisitInspector(ChartImpl context) {
        this.context = context;
        initComponent();
        update();
    }

    /**
     * レイアウトパネルを返す。
     * @return レイアウトパネル
     */
    public JPanel getPanel() {
        return calendarCardPanel;
    }

    /**
     * GUIコンポーネントを初期化する。
     */
    private void initComponent() {
        pvtEvent = "PVT"; // PVT
        calendarCardPanel = new CalendarCardPanel(ClientContext.getEventColorTable());
        calendarCardPanel.setCalendarRange(new int[]{-12, 0});
        
        // コンポーネント高固定のしるしをつける
        calendarCardPanel.setName(GUIConst.FIXED_HEIGHT);
    }

    private void update() {

        // 来院歴を取り出す
        List<String> latestVisit = context.getKarte().getPatientVisits();

        // 来院歴
        if (latestVisit != null && latestVisit.size() > 0) {
            ArrayList<SimpleDate> simpleDates = new ArrayList<>(latestVisit.size());
            for (String pvtDate : latestVisit) {
                SimpleDate sd = SimpleDate.mmlDateToSimpleDate(pvtDate);
                sd.setEventCode(pvtEvent);
                simpleDates.add(sd);
            }
            // CardCalendarに通知する
            calendarCardPanel.setMarkList(simpleDates);
        }
    }
}

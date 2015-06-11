package code2html;

import gnu.regexp.CharIndexed;
import java.io.Serializable;
import javax.swing.text.Segment;

public class CharIndexedSegment
    implements CharIndexed, Serializable
{

    public CharIndexedSegment(Segment seg, int index)
    {
        this.seg = seg;
        m_index = index;
    }

    public CharIndexedSegment(Segment seg, boolean reverse)
    {
        this.seg = seg;
        m_index = reverse ? seg.count - 1 : 0;
        this.reverse = reverse;
    }

    public char charAt(int index)
    {
        if(reverse)
            index = -index;
        return m_index + index >= seg.count || m_index + index < 0 ? '\uFFFF' : seg.array[seg.offset + m_index + index];
    }

    public boolean isValid()
    {
        return m_index >= 0 && m_index < seg.count;
    }

    public void reset()
    {
        m_index = reverse ? seg.count - 1 : 0;
    }

    public boolean move(int index)
    {
        if(reverse)
            index = -index;
        return (m_index += index) < seg.count;
    }

    private Segment seg;
    private int m_index;
    private boolean reverse;
}

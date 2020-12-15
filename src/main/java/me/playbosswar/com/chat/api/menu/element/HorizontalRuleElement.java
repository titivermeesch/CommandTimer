package me.playbosswar.com.chat.api.menu.element;

import me.playbosswar.com.chat.api.menu.ChatMenuAPI;
import me.playbosswar.com.chat.api.menu.IElementContainer;
import me.playbosswar.com.chat.api.util.Text;
import me.playbosswar.com.chat.api.util.TextUtil;

import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.List;

public class HorizontalRuleElement extends Element
{
	private final String text;
	private final int    width;
	
	/**
	 * Constructs a {@code HorizontalRuleElement} at the provided Y coordinate and a width of {@code 320} (default chat width).
	 *
	 * @param y the y coordinate to put this element at
	 */
	public HorizontalRuleElement(int y)
	{
		this(0, y, 320);
	}
	
	/**
	 * Constructs a {@code HorizontalRuleElement}.
	 *
	 * @param x     the x coordinate to put this element at
	 * @param y     the y coordinate to put this element at
	 * @param width the width of this element
	 */
	public HorizontalRuleElement(int x, int y, int width)
	{
		super(x, y);
		this.text = "\u00a7m" + TextUtil.generateWidth(' ', width, false);
		this.width = ChatMenuAPI.getWidth(text);
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return 1;
	}
	
	public List<Text> render(IElementContainer context)
	{
		return Collections.singletonList(new Text(text));
	}
	
	public void edit(@NotNull IElementContainer container, @NotNull String[] args)
	{
	
	}
}

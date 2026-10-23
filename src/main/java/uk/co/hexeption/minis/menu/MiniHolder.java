package uk.co.hexeption.minis.menu;

import org.jetbrains.annotations.Nullable;
import uk.co.hexeption.minis.entity.MiniEntity;

/**
 * MiniHolder
 *
 * A menu that's open on a Mini. The Mini is only known on the server.
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 */
public interface MiniHolder {

	@Nullable
	MiniEntity getMini();
}

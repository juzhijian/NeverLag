package cn.jiongjionger.neverlag.module.fastnms.v1_12_R1;

import net.minecraft.server.v1_12_R1.BlockChest;

public class BlockNormalChestInjector extends BlockChestInjector {

	public BlockNormalChestInjector() {
		super(BlockChest.Type.BASIC);
		c("chest");
	}

}

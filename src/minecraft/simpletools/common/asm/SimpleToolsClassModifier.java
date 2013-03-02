package simpletools.common.asm;

import static org.objectweb.asm.Opcodes.AALOAD;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IRETURN;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import cpw.mods.fml.relauncher.IClassTransformer;

public class SimpleToolsClassModifier implements IClassTransformer
{
	@Override
	public byte[] transform(String name, byte[] bytes)
	{
		System.out.println("Modifying ItemStack");
		ClassWriter cw = new ClassWriter(0);
		MethodVisitor mv;
		
		mv = cw.visitMethod(ACC_PUBLIC, "getDamageVsEntity", "(Lnet/minecraft/entity/Entity;)I", null, null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "hasTagCompound", "()Z");
		Label l0 = new Label();
		mv.visitJumpInsn(IFEQ, l0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getTagCompound", "()Lnet/minecraft/nbt/NBTTagCompound;");
		mv.visitLdcInsn("useDamageVsEntityTag");
		mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", "hasKey", "(Ljava/lang/String;)Z");
		mv.visitJumpInsn(IFEQ, l0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getTagCompound", "()Lnet/minecraft/nbt/NBTTagCompound;");
		mv.visitLdcInsn("useDamageVsEntityTag");
		mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", "getBoolean", "(Ljava/lang/String;)Z");
		mv.visitJumpInsn(IFEQ, l0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getTagCompound", "()Lnet/minecraft/nbt/NBTTagCompound;");
		mv.visitLdcInsn("damageVsEntity");
		mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", "getCompoundTag", "(Ljava/lang/String;)Lnet/minecraft/nbt/NBTTagCompound;");
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/Entity", "getEntityName", "()Ljava/lang/String;");
		mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", "hasKey", "(Ljava/lang/String;)Z");
		Label l1 = new Label();
		mv.visitJumpInsn(IFEQ, l1);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getTagCompound", "()Lnet/minecraft/nbt/NBTTagCompound;");
		mv.visitLdcInsn("damageVsEntity");
		mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", "getCompoundTag", "(Ljava/lang/String;)Lnet/minecraft/nbt/NBTTagCompound;");
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/Entity", "getEntityName", "()Ljava/lang/String;");
		mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", "getInteger", "(Ljava/lang/String;)I");
		mv.visitInsn(IRETURN);
		mv.visitLabel(l1);
		mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getTagCompound", "()Lnet/minecraft/nbt/NBTTagCompound;");
		mv.visitLdcInsn("damageVsEntity");
		mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", "getCompoundTag", "(Ljava/lang/String;)Lnet/minecraft/nbt/NBTTagCompound;");
		mv.visitLdcInsn("");
		mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", "getInteger", "(Ljava/lang/String;)I");
		mv.visitInsn(IRETURN);
		mv.visitLabel(l0);
		mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
		mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/Item", "itemsList", "[Lnet/minecraft/item/Item;");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, "net/minecraft/item/ItemStack", "itemID", "I");
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/Item", "getDamageVsEntity", "(Lnet/minecraft/entity/Entity;)I");
		mv.visitInsn(IRETURN);
		mv.visitMaxs(2, 2);
		mv.visitEnd();
		
		System.out.println("Successfully Modified ItemStack");
		return bytes;
	}
}

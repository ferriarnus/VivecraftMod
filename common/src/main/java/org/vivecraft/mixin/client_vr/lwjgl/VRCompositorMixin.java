package org.vivecraft.mixin.client_vr.lwjgl;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.lwjgl.PointerBuffer;
import org.lwjgl.openvr.VRCompositor;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.libffi.FFICIF;
import org.lwjgl.system.libffi.LibFFI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.vivecraft.client.utils.JNIUtils;

import java.nio.ByteBuffer;

@Mixin(VRCompositor.class)
public class VRCompositorMixin {
//    @WrapOperation(method = "nVRCompositor_SubmitWithArrayIndex", at = @At(value = "INVOKE", target = "Lorg/lwjgl/system/JNI;callPPI(IJIJIJ)I"), remap = false)
//    private static int vivecraft$nVRCompositor_SubmitWithArrayIndex(
//        int eEye, long pTexture, int unTextureArrayIndex, long pBounds, int nSubmitFlags, long __functionAddress,
//        Operation<Integer> original)
//    {
//        return JNIUtils.callI("IPUPI_I", __functionAddress, eEye, pTexture, unTextureArrayIndex, pBounds, nSubmitFlags);
//    }
//
    @WrapOperation(method = "nVRCompositor_Submit", at = @At(value = "INVOKE", target = "Lorg/lwjgl/system/JNI;callPPI(IJJIJ)I"), remap = false)
    private static int vivecraft$nVRCompositor_Submit(
        int eEye, long pTexture, long pBounds, int nSubmitFlags, long __functionAddress,
        Operation<Integer> original)
    {

        try (MemoryStack stack = MemoryStack.stackPush()) {
            FFICIF cif = FFICIF.malloc(stack);
            PointerBuffer argTypes = stack.mallocPointer(4);
            argTypes.put(LibFFI.ffi_type_sint32);
            argTypes.put(LibFFI.ffi_type_pointer);
            argTypes.put(LibFFI.ffi_type_pointer);
            argTypes.put(LibFFI.ffi_type_sint32);
            argTypes.flip();
            int ret = LibFFI.ffi_prep_cif(cif, LibFFI.FFI_DEFAULT_ABI, LibFFI.ffi_type_sint32, argTypes);

            PointerBuffer pointers = stack.mallocPointer(4);
            pointers.put(stack.ints(eEye));
            pointers.put(stack.pointers(pTexture).address());
            pointers.put(stack.pointers(pBounds).address());
            pointers.put(stack.ints(nSubmitFlags));
            pointers.flip();

            ByteBuffer returnValue = stack.malloc(Integer.BYTES);

            LibFFI.ffi_call(cif, __functionAddress, returnValue, pointers);

            return returnValue.getInt(0);
        }
        //return JNIUtils.callI("IPPI_I", __functionAddress, eEye, pTexture, pBounds, nSubmitFlags);
    }
//
//    @WrapOperation(method = "nVRCompositor_GetFrameTiming", at = @At(value = "INVOKE", target = "Lorg/lwjgl/system/JNI;callPZ(JIJ)Z"), remap = false)
//    private static boolean vivecraft$nVRCompositor_GetFrameTiming(
//        long pTiming, int unFramesAgo, long __functionAddress, Operation<Boolean> original)
//    {
//        return JNIUtils.callZ("PU_Z", __functionAddress, pTiming, unFramesAgo);
//    }
//
//    @WrapOperation(method = "VRCompositor_GetFrameTimeRemaining", at = @At(value = "INVOKE", target = "Lorg/lwjgl/system/JNI;callF(J)F"), remap = false)
//    private static float vivecraft$VRCompositor_GetFrameTimeRemaining(
//        long __functionAddress, Operation<Float> original)
//    {
//        return JNIUtils.callF("_F", __functionAddress);
//    }
//
//    @WrapOperation(method = "VRCompositor_FadeToColor", at = @At(value = "INVOKE", target = "Lorg/lwjgl/system/JNI;callV(FFFFFZJ)V"), remap = false)
//    private static void vivecraft$VRCompositor_FadeToColor(
//        float fSeconds, float fRed, float fGreen, float fBlue, float fAlpha, boolean bBackground,
//        long __functionAddress, Operation<Float> original)
//    {
//        JNIUtils.callV("FFFFFZ_F", __functionAddress, fSeconds, fRed, fGreen, fBlue, fAlpha, bBackground);
//    }
//
//    @WrapOperation(method = "VRCompositor_GetCurrentGridAlpha", at = @At(value = "INVOKE", target = "Lorg/lwjgl/system/JNI;callF(J)F"), remap = false)
//    private static float vivecraft$VRCompositor_GetCurrentGridAlpha(
//        long __functionAddress, Operation<Float> original)
//    {
//        return JNIUtils.callF("_F", __functionAddress);
//    }
//
//    @WrapOperation(method = "VRCompositor_IsFullscreen", at = @At(value = "INVOKE", target = "Lorg/lwjgl/system/JNI;callZ(J)Z"), remap = false)
//    private static boolean vivecraft$VRCompositor_IsFullscreen(long __functionAddress, Operation<Boolean> original) {
//        return JNIUtils.callZ("_Z", __functionAddress);
//    }
//
//    @WrapOperation(method = "VRCompositor_CanRenderScene", at = @At(value = "INVOKE", target = "Lorg/lwjgl/system/JNI;callZ(J)Z"), remap = false)
//    private static boolean vivecraft$VRCompositor_CanRenderScene(long __functionAddress, Operation<Boolean> original) {
//        return JNIUtils.callZ("_Z", __functionAddress);
//    }
//
//    @WrapOperation(method = "VRCompositor_IsMirrorWindowVisible", at = @At(value = "INVOKE", target = "Lorg/lwjgl/system/JNI;callZ(J)Z"), remap = false)
//    private static boolean vivecraft$VRCompositor_IsMirrorWindowVisible(
//        long __functionAddress, Operation<Boolean> original)
//    {
//        return JNIUtils.callZ("_Z", __functionAddress);
//    }
//
//    @WrapOperation(method = "VRCompositor_ShouldAppRenderWithLowResources", at = @At(value = "INVOKE", target = "Lorg/lwjgl/system/JNI;callZ(J)Z"), remap = false)
//    private static boolean vivecraft$VRCompositor_ShouldAppRenderWithLowResources(
//        long __functionAddress, Operation<Boolean> original)
//    {
//        return JNIUtils.callZ("_Z", __functionAddress);
//    }
//
//    @WrapOperation(method = "VRCompositor_IsMotionSmoothingEnabled", at = @At(value = "INVOKE", target = "Lorg/lwjgl/system/JNI;callZ(J)Z"), remap = false)
//    private static boolean vivecraft$VRCompositor_IsMotionSmoothingEnabled(
//        long __functionAddress, Operation<Boolean> original)
//    {
//        return JNIUtils.callZ("_Z", __functionAddress);
//    }
//
//    @WrapOperation(method = "VRCompositor_IsMotionSmoothingSupported", at = @At(value = "INVOKE", target = "Lorg/lwjgl/system/JNI;callZ(J)Z"), remap = false)
//    private static boolean vivecraft$VRCompositor_IsMotionSmoothingSupported(
//        long __functionAddress, Operation<Boolean> original)
//    {
//        return JNIUtils.callZ("_Z", __functionAddress);
//    }
//
//    @WrapOperation(method = "VRCompositor_IsCurrentSceneFocusAppLoading", at = @At(value = "INVOKE", target = "Lorg/lwjgl/system/JNI;callZ(J)Z"), remap = false)
//    private static boolean vivecraft$VRCompositor_IsCurrentSceneFocusAppLoading(
//        long __functionAddress, Operation<Boolean> original)
//    {
//        return JNIUtils.callZ("_Z", __functionAddress);
//    }
//
//    @WrapOperation(method = "nVRCompositor_GetCompositorBenchmarkResults", at = @At(value = "INVOKE", target = "Lorg/lwjgl/system/JNI;callPZ(JIJ)Z"), remap = false)
//    private static boolean vivecraft$nVRCompositor_GetCompositorBenchmarkResults(
//        long pBenchmarkResults, int nSizeOfBenchmarkResults, long __functionAddress, Operation<Boolean> original)
//    {
//        return JNIUtils.callZ("PU_Z", __functionAddress, pBenchmarkResults, nSizeOfBenchmarkResults);
//    }
}

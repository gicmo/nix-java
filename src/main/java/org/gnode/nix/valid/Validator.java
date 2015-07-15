package org.gnode.nix.valid;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.annotation.*;
import org.gnode.nix.*;

/**
 * <h1>Validator</h1>
 * Class used to validate entities.
 */

@Properties(value = {
        @Platform(include = {"<nix/valid/validate.hpp>",
                "<nix/Block.hpp>",
                "<nix/DataArray.hpp>",
                "<nix/Dimensions.hpp>",
                "<nix/Feature.hpp>",
                "<nix/File.hpp>",
                "<nix/MultiTag.hpp>",
                "<nix/Property.hpp>",
                "<nix/Section.hpp>",
                "<nix/Source.hpp>",
                "<nix/Tag.hpp>"}, link = "nix"),
        @Platform(value = "linux"),
        @Platform(value = "windows")})
@Namespace("nix::valid")
public class Validator {
    static {
        Loader.load();
    }

    /**
     * Block entity validator
     * <p>
     * Function taking a Block entity and returning {@link Result} object
     *
     * @param block Block entity
     * @return The validation results as {@link Result} object
     * @see Result
     */
    public static native
    @ByVal
    Result
    validate(@Const @ByRef Block block);

    /**
     * DataArray entity validator
     * <p>
     * Function taking a DataArray entity and returning {@link Result}
     * object
     *
     * @param dataArray DataArray entity
     * @return The validation results as {@link Result} object
     * @see Result
     */
    public static native
    @ByVal
    Result
    validate(@Const @ByRef DataArray dataArray);

    /**
     * Tag entity validator
     * <p>
     * Function taking a Tag entity and returning {@link Result}
     * object
     *
     * @param tag Tag entity
     * @return The validation results as {@link Result} object
     * @see Result
     */
    public static native
    @ByVal
    Result
    validate(@Const @ByRef Tag tag);

    /**
     * Property entity validator
     * <p>
     * Function taking a Property entity and returning {@link Result}
     * object
     *
     * @param property Property entity
     * @return The validation results as {@link Result} object
     * @see Result
     */
    public static native
    @ByVal
    Result
    validate(@Const @ByRef Property property);

    /**
     * MultiTag entity validator
     * <p>
     * Function taking a MultiTag entity and returning {@link Result} object
     *
     * @param multiTag MultiTag entity
     * @return The validation results as {@link Result} object
     * @see Result
     */
    public static native
    @ByVal
    Result
    validate(@Const @ByRef MultiTag multiTag);

    /**
     * Dimension entity validator
     * <p>
     * Function taking a Dimension entity and returning {@link Result}
     * object
     *
     * @param dimension Dimension entity
     * @return The validation results as {@link Result} object
     * @see Result
     */
    public static native
    @ByVal
    Result
    validate(@Const @ByRef Dimension dimension);

    /**
     * RangeDimension entity validator
     * <p>
     * Function taking a RangeDimension entity and returning {@link Result}
     * object
     *
     * @param rangeDimension RangeDimension entity
     * @return The validation results as {@link Result} object
     * @see Result
     */
    public static native
    @ByVal
    Result
    validate(@Const @ByRef RangeDimension rangeDimension);

    /**
     * SampledDimension entity validator
     * <p>
     * Function taking a SampledDimension entity and returning
     * {@link Result} object
     *
     * @param sampledDimension SampledDimension entity
     * @return The validation results as {@link Result} object
     * @see Result
     */
    public static native
    @ByVal
    Result
    validate(@Const @ByRef SampledDimension sampledDimension);

    /**
     * SetDimension entity validator
     * <p>
     * Function taking a SetDimension entity and returning {@link Result}
     * object
     *
     * @param setDimension SetDimension entity
     * @return The validation results as {@link Result} object
     * @see Result
     */
    public static native
    @ByVal
    Result
    validate(@Const @ByRef SetDimension setDimension);

    /**
     * Feature entity validator
     * <p>
     * Function taking a Feature entity and returning {@link Result} object
     *
     * @param feature Feature entity
     * @return The validation results as {@link Result} object
     * @see Result
     */
    public static native
    @ByVal
    Result
    validate(@Const @ByRef Feature feature);

    /**
     * Section entity validator
     * <p>
     * Function taking a Section entity and returning {@link Result} object
     *
     * @param section Section entity
     * @return The validation results as {@link Result} object
     * @see Result
     */
    public static native
    @ByVal
    Result
    validate(@Const @ByRef Section section);

    /**
     * Source entity validator
     * <p>
     * Function taking a Source entity and returning {@link Result} object
     *
     * @param source Source entity
     * @return The validation results as {@link Result} object
     * @see Result
     */
    public static native
    @ByVal
    Result
    validate(@Const @ByRef Source source);

    /**
     * File entity validator
     * <p>
     * Function taking a File entity and returning {@link Result} object
     *
     * @param file File entity
     * @return The validation results as {@link Result} object
     * @see Result
     */
    public static native
    @ByVal
    Result
    validate(@Const @ByRef File file);
}

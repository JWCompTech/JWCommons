package com.jwcomptech.commons.utils.osutils.windows.enums;

/*-
 * #%L
 * JWCT Commons
 * %%
 * Copyright (C) 2025 JWCompTech
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import com.jwcomptech.commons.enums.BaseEnum;
import com.jwcomptech.commons.values.StringValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * A list of Product Editions according to
 * <a href="http://msdn.microsoft.com/en-us/library/ms724358(VS.85).aspx">Microsoft Documentation</a>.
 *
 * @since 0.0.1
 */
@AllArgsConstructor
@Getter
@ToString
public enum ProductEdition implements BaseEnum<Integer> {
    /** Business. */
    Business(6, "Business"),
    /** BusinessN. */
    BusinessN(16, "Business N"),
    /** ClusterServer. */
    ClusterServer(18, "HPC Edition"),
    /** ClusterServerV. */
    ClusterServerV(64, "Hyper Core V"),
    /** DatacenterServer. */
    DatacenterServer(8, "Datacenter"),
    /** DatacenterServerCore. Windows Server 2008 R2 and earlier. */
    DatacenterServerCore(12, "Datacenter (Core installation)"),
    /** DatacenterServerCoreA */
    DatacenterServerCoreA(145, "Datacenter Semi-Annual Channel (Core installation)"),
    /** DatacenterServerCoreV. */
    DatacenterServerCoreV(39, "Datacenter without Hyper-V (Core installation)"),
    /** DatacenterServerEval */
    DatacenterServerEval(80, "Datacenter (Evaluation installation)"),
    /** DatacenterServerV. */
    DatacenterServerV(37, "Datacenter without Hyper-V"),

    /** Education. */
    Education(121, "Education"),
    /** Education N. */
    EducationN(122, "Education N"),
    /** Enterprise. */
    Enterprise(4, "Enterprise"),
    /** EnterpriseE. */
    EnterpriseE(70, "Enterprise E"),
    /** EnterpriseEval. */
    EnterpriseEval(72, "Enterprise Evaluation"),
    /** EnterpriseN. */
    EnterpriseN(27, "Enterprise N"),
    /** EnterpriseNEval. */
    EnterpriseNEval(84, "Enterprise N Evaluation"),
    /** EnterpriseS. */
    EnterpriseS(125, "Enterprise 2015 LTSB"),
    /** EnterpriseServer. */
    EnterpriseServer(10, "Enterprise Server"),
    /** EnterpriseServerCore. */
    EnterpriseServerCore(14, "Enterprise (Core installation)"),
    /** EnterpriseServerCoreV. */
    EnterpriseServerCoreV(41, "Enterprise without Hyper-V (Core installation)"),
    /** EnterpriseServerIA64. */
    EnterpriseServerIA64(15, "Enterprise For Itanium-based Systems"),
    /** EnterpriseServerV. */
    EnterpriseServerV(38, "Enterprise without Hyper-V"),
    /** EnterpriseSEval. */
    EnterpriseSEval(129, "Enterprise 2015 LTSB Evaluation"),
    /** EnterpriseSN. */
    EnterpriseSN(126, "Enterprise 2015 LTSB N"),
    /** EnterpriseSNEval. */
    EnterpriseSNEval(130, "Enterprise 2015 LTSB N Evaluation"),
    /** EnterpriseVirtual. */
    EnterpriseVirtual(175, "Enterprise for Virtual Desktops"),

    /** Home */
    Home(101, "Home"),
    /** HomeBasic. */
    HomeBasic(2, "Home Basic"),
    /** HomeBasicE. */
    HomeBasicE(67, "Home Basic E"),
    /** HomeBasicN. */
    HomeBasicN(5, "Home Basic N"),
    /** Home China */
    HomeChina(99, "Home China"),
    /** Home N*/
    HomeN(98, "Home N"),
    /** HomePremium. */
    HomePremium(3, "Home Premium"),
    /** HomePremiumE. */
    HomePremiumE(68, "Home Premium E"),
    /** HomePremiumN. */
    HomePremiumN(26, "Home Premium N"),
    /** Storage Server 2008 R2 Essentials */
    HomeServer(19, "Storage Server 2008 R2 Essentials"),
    /** Home Server 2011 */
    HomeServer2011(34, "Home Server 2011"),
    /** Home Single Language*/
    HomeSingleLanguage(100, "Home Single Language"),
    /** HyperV*/
    HyperV(42, "Hyper V"),

    /** MediumBusinessServerManagement. */
    MediumBusinessServerManagement(30, "Windows Essential Business Management"),
    /** MediumBusinessServerMessaging. */
    MediumBusinessServerMessaging(32, "Windows Essential Business Messaging"),
    /** MediumBusinessServerSecurity. */
    MediumBusinessServerSecurity(31, "Windows Essential Business Security"),
    /** MultiPoint Premium */
    MultiPointStandard(76, "MultiPoint Server Premium"),
    /** MultiPoint Premium */
    MultiPointPremium(77, "MultiPoint Server Premium"),

    /** ProEducation. */
    ProEducation(164, "Pro Education"),
    /** Professional. */
    Professional(48, "Professional"),
    /** ProfessionalE. */
    ProfessionalE(69, "Professional E"),
    /** ProfessionalN. */
    ProfessionalN(49, "Professional N"),
    /** ProfessionalE. */
    ProWorkstation(161, "Pro Workstation"),
    /** ProWorkstationN. */
    ProWorkstationN(162, "Pro Workstation N"),

    /** SBSolutionServer. */
    SBSolutionServer(50, "Small Business Server 2011 Essentials"),
    /** ServerForSmallBusiness. */
    ServerForSmallBusiness(24, "Windows Essential Server Solutions"),
    /** ServerForSmallBusinessV. */
    ServerForSmallBusinessV(35, "Windows Essential Server Solutions without Hyper-V"),
    /** SmallBusinessServer. */
    SmallBusinessServer(9, "Small Business Server"),
    /** SmallBusinessServerPremium. */
    SmallBusinessServerPremium(25, "Small Business Server Premium"),
    /** SmallBusinessServerPremiumCore. */
    SmallBusinessServerPremiumCore(63, "Small Business Server Premium Core"),
    /** StandardServer. */
    StandardServer(7, "Standard"),
    /** StandardServerCore. Windows Server 2008 R2 and earlier. */
    StandardServerCore(13, "Standard (Core installation)"),
    /** DatacenterServerCoreA */
    StandardServerCoreA(146, "Standard Semi-Annual Channel (Core installation)"),
    /** StandardServerCoreV. */
    StandardServerCoreV(40, "Standard without Hyper-V (Core installation)"),
    /** StandardServerV. */
    StandardServerV(36, "Standard without Hyper-V"),
    /** Starter. */
    Starter(11, "Starter"),
    /** StarterE. */
    StarterE(66, "Starter E"),
    /** StarterN. */
    StarterN(47, "Starter N"),
    /** StorageEnterpriseServer. */
    StorageEnterpriseServer(23, "Storage Enterprise"),
    /** StorageEnterpriseServerCore. */
    StorageEnterpriseServerCore(46, "Storage Enterprise (Core installation)"),
    /** StorageExpressServer. */
    StorageExpressServer(20, "Storage Express"),
    /** StorageExpressServerCore. */
    StorageExpressServerCore(43, "Storage Express (Core installation)"),
    /** StorageStandardServer. */
    StorageStandardServer(21, "Storage Standard"),
    /** StorageStandardServer. */
    StorageStandardServerCore(44, "Storage Standard (Core installation)"),
    /** StorageStandardServerEval. */
    StorageStandardServerEval(96, "Storage Standard Eval"),
    /** StorageWorkgroupServer. */
    StorageWorkgroupServer(22, "Storage Workgroup"),
    /** StorageWorkgroupServerCore. */
    StorageWorkgroupServerCore(45, "Storage Workgroup (Core installation)"),
    /** StorageWorkgroupServer. */
    StorageWorkgroupServerEval(95, "Storage Workgroup Eval"),

    /** Ultimate. */
    Ultimate(1,"Ultimate"),
    /** UltimateE. */
    UltimateE(71,"Ultimate E"),
    /** UltimateN. */
    UltimateN(28,"Ultimate N"),

    /** WebServer. */
    WebServer(17, "Web Server"),
    /** WebServerCore. */
    WebServerCore(29, "Web (Core installation)"),

    /** Undefined. */
    Undefined(0,"Unknown Product");

    //Must be Integer not int or compilation fails
    private final Integer value;
    private final String fullName;

    @Contract(" -> new")
    public @NotNull StringValue getFullName() {
        return StringValue.of(fullName);
    }

    public static ProductEdition parse(final int value) {
        return Arrays.stream(ProductEdition.values())
                .filter(type -> type.value == value)
                .findFirst()
                .orElse(Undefined);
    }

    public static ProductEdition parseString(final String name) {
        return Arrays.stream(ProductEdition.values())
                .filter(type -> type.fullName.equalsIgnoreCase(name))
                .findFirst()
                .orElse(Undefined);
    }
}
